import * as postService from '../service/postService.js';
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    document.getElementById('post-submit-btn')
        ?.addEventListener('click', createPost);

    document.getElementById('post-update-btn')
        ?.addEventListener('click', updatePost)
}

async function createPost()
{
    const form = {
        categoryId: document.getElementById('categoryId').value,
        title: document.getElementById('title').value,
        authorId: document.getElementById('authorId').value,
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

async function updatePost()
{
    const postId = document.getElementById('postId').value;

    const form = {
        postId: document.getElementById('postId').value,
        categoryId: document.getElementById('categoryId').value,
        title: document.getElementById('title').value,
        authorId: document.getElementById('authorId').value,
        anonymous: document.getElementById('anonymousToggle').checked,
        tags: document.getElementById('tag-hidden-input').value,
        content: document.getElementById('content').value
    }

    try
    {
        await postService.updatePost(form);
        alert('수정이 완료되었습니다.');
        window.location.href = `/post/${postId}`;
    }
    catch (error)
    {
        handleError(error);
    }
}

