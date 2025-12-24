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
    // 현재 화면의 제목과 태그를 status에 동기화
    status.previousTitle = document.getElementById('title').value || '';
    status.previousTags = tagUI.getTags();

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

// postId 확인 (수정 모드)
    const postIdElement = document.getElementById('postId');
    const postId = postIdElement ? postIdElement.value : null;

    const form = {
        authorId: document.getElementById('author-id').value,
        postId: postId, // 수정시 postId 포함
        title: document.getElementById('title').value,
        tags: document.getElementById('tag-hidden-input').value,
        content: currentDelta ? JSON.stringify(currentDelta) : JSON.stringify(document.getElementById('content').value),
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
    // AI가 제목을 제공하지 않았거나 빈 값인 경우 기존 제목 유지
    if (!post.title || post.title.trim() === '') return;

    if (post.title !== status.previousTitle)
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

    // AI가 태그를 제공하지 않았고(빈 배열), 기존에 태그가 있는 경우 기존 태그 유지
    if (newTags.length === 0 && status.previousTags.length > 0) return;

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

// 마지막 content 업데이트 추적용
let contentUpdateCounter = 0;

async function updateContent(post)
{
    if (post.content === undefined) return;

    // 같은 content면 스킵
    if (post.content === status.previousContent) return;

    const currentUpdate = ++contentUpdateCounter;
    console.log(`[updateContent #${currentUpdate}] 시작, content 길이: ${post.content.length}`);

    const deltaOps = chatService.parseContent(post.content);

    console.log(`[updateContent #${currentUpdate}] parseContent 결과:`, deltaOps ? '성공' : '실패 (null)');

    if (!deltaOps)
    {
        console.log(`[updateContent #${currentUpdate}] deltaOps가 null이라 스킵`);
        return;
    }

    // deltaOps가 배열이므로 {ops: [...]} 형태로 변환
    const delta = Array.isArray(deltaOps) ? {ops: deltaOps} : deltaOps;

    console.log(`[updateContent #${currentUpdate}] delta 생성됨, ops 개수: ${delta.ops?.length}`);

    // 이미지 URL 검증 (비동기)
    const sanitizedDelta = await sanitizeDelta(delta);

    // 비동기 작업 후 다른 업데이트가 있었는지 확인
    if (currentUpdate !== contentUpdateCounter)
    {
        console.log(`[updateContent #${currentUpdate}] 새 업데이트(#${contentUpdateCounter})가 있어서 스킵`);
        return;
    }

    const quill = quillUI.getQuill();

    console.log(`[updateContent #${currentUpdate}] quill 존재: ${!!quill}`);

    if (quill)
    {
        console.log(`[updateContent #${currentUpdate}] setContents 호출`);
        quill.setContents(sanitizedDelta, 'silent');
        document.getElementById('content').value = quill.root.innerHTML;
        console.log(`[updateContent #${currentUpdate}] setContents 완료`);
    }

    status.previousContent = post.content;
    console.log(`[updateContent #${currentUpdate}] 완료`);
}

function updateMessage(post)
{
    if (post.message !== undefined && post.message !== status.previousMessage)
    {
        document.getElementById(status.assistantMessageTextId).textContent = post.message;
        status.previousMessage = post.message;
    }
}