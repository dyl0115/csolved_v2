import * as postService from '../service/postService.js';
import {handleError} from "../../common/error/errorHandler.js";


export function init()
{
    document.querySelector('.post-submit-btn')?.addEventListener('click', postSubmit);
}

async function postSubmit()
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