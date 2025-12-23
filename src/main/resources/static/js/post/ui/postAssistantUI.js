import * as postService from '../service/postService.js';
import {handleError} from "../../global/error/errorHandler.js";

let isResizing = false;

export function init()
{
    document.getElementById('post-submit-btn')
        ?.addEventListener('click', postCreate);

    document.getElementById('post-update-btn')
        ?.addEventListener('click', postUpdate);

    document.getElementById('resizer')
        ?.addEventListener('mousedown', event => startResize(event))

    document.addEventListener('mousemove', event => onResize(event));

    document.addEventListener('mouseup', stopResize);
}

async function postCreate()
{
    const form = {
        categoryId: document.getElementById('categoryId').value,
        title: document.getElementById('title').value,
        authorId: document.getElementById('author-id').value,
        anonymous: document.getElementById('anonymous').checked,
        tags: document.getElementById('tag-hidden-input').value,
        content: document.getElementById('content').value
    };

    try
    {
        await postService.createPost(form);
        alert('작성이 완료되었습니다.');
        window.location.href = '/post/list?page=1';
    }
    catch (error)
    {
        handleError(error);
    }
}

async function postUpdate()
{
    const form = {
        postId: document.getElementById('post-id').value,
        categoryId: document.getElementById('categoryId').value,
        title: document.getElementById('title').value,
        authorId: document.getElementById('author-id').value,
        anonymous: document.getElementById('anonymous').checked,
        tags: document.getElementById('tag-hidden-input').value,
        content: document.getElementById('content').value
    };

    try
    {
        await postService.updatePost(form);
        alert('작성이 완료되었습니다.');
        window.location.href = '/post/list?page=1';
    }
    catch (error)
    {
        handleError(error);
    }
}

function startResize(event)
{
    isResizing = true;

    event.preventDefault();

    const resizer = document.getElementById('resizer');
    const resizerBar = document.getElementById('resizer-bar');

    resizer.classList.add('bg-blue-200');
    resizerBar.classList.remove('bg-gray-400', 'w-[3px]');
    resizerBar.classList.add('bg-blue-500', 'w-1');

    document.body.style.cursor = 'col-resize';
    document.body.style.userSelect = 'none';
}

function onResize(event)
{
    if (!isResizing) return;

    event.preventDefault();

    const mainContainer = document.getElementById('main-container');
    const formContainer = document.getElementById('form-container');
    const aiContainer = document.getElementById('ai-container');
    const containerRect = mainContainer.getBoundingClientRect();
    const containerWidth = containerRect.width;
    const resizerWidth = 12;

    let formWidth = event.clientX - containerRect.left;

    const minWidth = containerWidth * 0.3;
    const maxWidth = containerWidth * 0.7;

    if (formWidth < minWidth) formWidth = minWidth;
    if (formWidth > maxWidth) formWidth = maxWidth;

    const aiWidth = containerWidth - formWidth - resizerWidth;

    formContainer.style.width = `${formWidth}px`;
    aiContainer.style.width = `${aiWidth}px`;
}

function stopResize()
{
    if (!isResizing) return;
    isResizing = false;

    const resizer = document.getElementById('resizer');
    const resizerBar = document.getElementById('resizer-bar');

    resizer.classList.remove('bg-blue-200');
    resizerBar.classList.remove('bg-blue-500', 'w-1');
    resizerBar.classList.add('bg-gray-400', 'w-[3px]');

    document.body.style.cursor = 'default';
    document.body.style.userSelect = 'auto';
}