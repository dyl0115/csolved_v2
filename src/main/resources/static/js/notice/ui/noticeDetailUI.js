import * as noticeService from '../service/noticeService.js';
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    document.getElementById('notice-menu-btn')
        ?.addEventListener('click', toggleNoticeMenu);

    document.getElementById('notice-like-btn')
        ?.addEventListener('click', addLike);

    document.getElementById('notice-delete-btn')
        ?.addEventListener('click', deleteNotice);

    document.addEventListener('click', closeAllToggleMenu);
}

function toggleNoticeMenu(event)
{
    event.stopPropagation();

    const noticeMenu = document.getElementById('notice-menu');
    const isHidden = noticeMenu.classList.contains('hidden');

    closeAllToggleMenu();

    if (isHidden) noticeMenu.classList.remove('hidden');
}

function closeAllToggleMenu()
{
    document.querySelectorAll('.toggle-menu')
        .forEach(menu => menu.classList.add('hidden'));
}

async function deleteNotice()
{
    const noticeId = document.getElementById('post-id').textContent;

    if (confirm('게시글을 삭제하겠습니까?'))
    {
        try
        {
            alert("삭제가 완료되었습니다.");
            await noticeService.deleteNotice(noticeId);
            window.location.href = '/notices?page=1';
        }
        catch (error)
        {
            handleError(error);
        }
    }
}

async function addLike()
{
    const noticeId = document.getElementById('post-id').textContent;
    const likeCount = document.getElementById('like-count');
    let likeCountInt = parseInt(likeCount.textContent, 10) || 0;

    try
    {
        likeCount.textContent = ++likeCountInt;
        await noticeService.addLike(noticeId);
    }
    catch (error)
    {
        likeCount.textContent = --likeCountInt;
        handleError(error);
    }

}