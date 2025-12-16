import * as noticeService from '../service/noticeService.js';
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    document.getElementById('notice-create-btn')
        ?.addEventListener('click', createNotice);

    document.getElementById('notice-update-btn')
        ?.addEventListener('click', updateNotice);
}

async function createNotice()
{
    const form = {
        categoryId: document.getElementById('categoryId').value,
        title: document.getElementById('title').value,
        authorId: document.getElementById('authorId').value,
        anonymous: document.getElementById('anonymous').checked,
        content: document.getElementById('content').value
    }

    try
    {
        await noticeService.createNotice(form);
        alert('작성이 완료되었습니다.');
        window.location.href = '/notices?page=1';
    }
    catch (error)
    {
        handleError(error);
    }
}

async function updateNotice()
{
    const noticeId = document.getElementById('noticeId').value;

    const form = {
        categoryId: document.getElementById('categoryId').value,
        noticeId: noticeId,
        title: document.getElementById('title').value,
        authorId: document.getElementById('authorId').value,
        anonymous: document.getElementById('anonymousToggle').checked,
        content: document.getElementById('content').value
    }

    try
    {
        await noticeService.updateNotice(form);
        alert('수정이 완료되었습니다.');
        window.location.href = `/notice/${noticeId}`;
    }
    catch (error)
    {
        handleError(error);
    }
}