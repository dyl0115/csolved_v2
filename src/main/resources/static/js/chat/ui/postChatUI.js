import * as chatService from "../service/chatService.js";
import * as quillUI from "../../editor/quill.js";
import * as tagUI from '../../global/utils/tagUI.js';

const status = {
    previousTitle: '',
    previousTags: [],
    previousContent: '',
    previousMessage: '',
    assistantMessageCount: 0,
    assistantMessageTextId: '',
}

export function init()
{
    chatService.handleStream((post) =>
    {
        updateTitle(post);
        updateTags(post);
        updateMessage(post);
        updateContent(post);
    });

    document.getElementById('chat-submit-btn').addEventListener('click', sendChatMessage);

    document.getElementById('chat-input').addEventListener('keydown', function (event)
    {
        if (event.key === 'Enter' && !event.shiftKey)
        {
            event.preventDefault();
            sendChatMessage();
        }
    })
}

export function sendChatMessage()
{
    const messageInput = document.getElementById('chat-input');

    const message = messageInput.value.trim();
    if (!message) return;

    const quill = quillUI.getQuill();

    // Quill에서 직접 Delta 가져오기
    const currentDelta = quill ? quill.getContents() : null;

    const form = {
        title: document.getElementById('title').value,
        content: currentDelta ? JSON.stringify(currentDelta) : JSON.stringify(document.getElementById('content').value),
        tags: document.getElementById('tag-hidden-input').value,
        message: message,
    }

    addUserMessage(message);
    messageInput.value = '';
    addAssistantMessage('생각 중...');

    chatService.sendMessage(form);
}

function addUserMessage(textContent)
{
    const chatMessages = document.getElementById('chat-messages');
    const template = document.getElementById('user-message-template');
    const clone = template.content.cloneNode(true);
    const messageText = clone.querySelector('.message-text');
    messageText.textContent = textContent;
    chatMessages.appendChild(clone);
    chatMessages.scrollTop = chatMessages.scrollHeight;
    lucide.createIcons();
}

function addAssistantMessage(textContent)
{
    const chatMessages = document.getElementById('chat-messages');
    const template = document.getElementById('assistant-message-template');
    const clone = template.content.cloneNode(true);
    const messageText = clone.querySelector('.message-text');
    messageText.id = `assistant-message-${++status.assistantMessageCount}`;
    messageText.textContent = textContent;
    status.assistantMessageTextId = messageText.id;
    chatMessages.appendChild(clone);
    chatMessages.scrollTop = chatMessages.scrollHeight;
    lucide.createIcons();
}

function updateTitle(post)
{
    if (post.title !== undefined && post.title !== status.previousTitle)
    {
        document.getElementById('title').value = post.title;
        status.previousTitle = post.title;
    }
}

function updateTags(post)
{
    if (post.tags === undefined || !Array.isArray(post.tags)) return;

    // 빈 문자열 제거
    const newTags = post.tags.filter(tag => tag && tag.trim() !== '');
    const previousTags = status.previousTags;

    // 변경사항이 없으면 스킥
    if (JSON.stringify(newTags) === JSON.stringify(previousTags)) return;

    // 새로 추가된 태그
    const tagsToAdd = newTags.filter(tag => !previousTags.includes(tag));

    // 삭제된 태그
    const tagsToRemove = previousTags.filter(tag => !newTags.includes(tag));

    // UI 업데이트
    tagsToRemove.forEach(tag =>
    {
        tagUI.removeTag(tag);
    });

    tagsToAdd.forEach(tag =>
    {
        tagUI.addTag(tag);
    });

    // 상태 업데이트
    status.previousTags = [...newTags];
}

async function validateImageUrl(url)
{
    return new Promise((resolve) =>
    {
        const img = new Image();
        img.onload = () => resolve(true);
        img.onerror = () => resolve(false);
        img.src = url;

        // 5초 타임아웃
        setTimeout(() => resolve(false), 5000);
    });
}

async function sanitizeDelta(delta)
{
    if (!delta || !delta.ops) return delta;

    const ops = delta.ops;
    const validatedOps = [];

    for (const op of ops)
    {
        if (op.insert && typeof op.insert === 'object' && op.insert.image)
        {
            // 이미지 URL 검증
            const isValid = await validateImageUrl(op.insert.image);
            if (isValid)
            {
                validatedOps.push(op);
            } else
            {
                // 깨진 이미지 대신 텍스트로 표시
                validatedOps.push({
                    insert: `[이미지 로드 실패: ${op.insert.image}]\n`,
                    attributes: {color: '#888', italic: true}
                });
            }
        } else if (op.insert && typeof op.insert === 'object' && op.insert.video)
        {
            // 비디오는 검증 없이 그대로 통과
            validatedOps.push(op);
        } else
        {
            validatedOps.push(op);
        }
    }

    return {ops: validatedOps};
}

async function updateContent(post)
{
    if (post.content === undefined || post.content === status.previousContent) return;

    const deltaOps = chatService.parseContent(post.content);
    if (deltaOps)
    {
        // 이미지 URL 검증
        const sanitizedDelta = await sanitizeDelta(deltaOps);

        const quill = quillUI.getQuill();

        if (quill)
        {
            quill.setContents(sanitizedDelta, 'silent');
            // hidden input도 업데이트 (수동으로)
            document.getElementById('content').value = quill.root.innerHTML;
        }

        status.previousContent = post.content;
    }
}

function updateMessage(post)
{
    if (post.message !== undefined && post.message !== status.previousMessage)
    {
        document.getElementById(status.assistantMessageTextId).textContent = post.message;
        status.previousMessage = post.message;
    }
}