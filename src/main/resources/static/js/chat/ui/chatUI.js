import * as chatService from "../service/chatService.js";

const status = {
    previousTitle: '',
    previousTags: [],
    previousContent: '',
    previousMessage: '',
    assistantMessageCount: 0,
    assistantMessageTextId: '',
}

export function initChatUI()
{
    chatService.handleStream((post) =>
    {
        console.log("chatUI: " + JSON.stringify(post));
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

    const form = {
        title: document.getElementById('title').value,
        content: JSON.stringify(quill.getContents()),
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
    if (post.tags !== undefined)
    {
        const tagsString = JSON.stringify(post.tags);
        const previousTagsString = JSON.stringify(status.previousTags);

        if (tagsString !== previousTagsString && Array.isArray(post.tags))
        {
            document.getElementById('tag-hidden-input').value = post.tags.join(',');
            status.previousTags = [...post.tags];
        }
    }
}

function updateContent(post)
{
    let deltaOps;

    try
    {
        // 1단계: post.content 문자열로 파싱 시도
        const contentDelta = JSON.parse(post.content);

        if (contentDelta?.ops && Array.isArray(contentDelta.ops))
        {
            deltaOps = contentDelta.ops;
        }
    }
    catch (error)
    {
        // 2단계: 파싱 실패 시 repair 후 다시 파싱
        try
        {
            const repairedContent = window.jsonRepair(post.content);
            console.log("repairedContent: " + repairedContent);

            // repair된 문자열을 다시 파싱
            const delta = JSON.parse(repairedContent);

            if (delta?.ops && Array.isArray(delta.ops))
            {
                deltaOps = delta.ops;
            }
        }
        catch (error2)
        {
            console.warn("Delta repair/parse failed:", error2);
            return;
        }

        if (deltaOps)
        {
            quill.setContents(deltaOps);
            status.previousContent = post.content;
        }
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