import * as postService from '../service/postService.js';
import {handleError} from '../../global/error/errorHandler.js';

export function init()
{
    document.getElementById('post-delete-btn')
        ?.addEventListener('click', deletePost);

    document.getElementById('post-like-btn')
        ?.addEventListener('click', addPostLike);

    document.getElementById('bookmark-btn')
        ?.addEventListener('click', toggleBookmark);

    document.getElementById('post-menu-btn')
        ?.addEventListener('click', togglePostMenu);

    document.addEventListener('click', closeAllToggleMenu);
}

async function deletePost()
{
    const postId = document.getElementById('postId').textContent;

    if (confirm('게시글을 삭제하시겠습니까?'))
    {
        try
        {
            await postService.deletePost(postId);
            alert('삭제가 완료되었습니다.');
            window.location.href = '/post/list?page=1';
        }
        catch (error)
        {
            handleError(error);
        }
    }
}

async function addPostLike()
{
    const postId = document.getElementById('postId').textContent;
    const likeCount = document.getElementById('post-like-count');
    let likeCountInt = parseInt(likeCount.textContent, 10) || 0;

    try
    {
        likeCount.textContent = ++likeCountInt;
        await postService.addPostLike(postId);
    }
    catch (error)
    {
        likeCount.textContent = --likeCountInt;
        handleError(error);
    }
}

async function toggleBookmark()
{
    const postId = document.getElementById('postId').textContent;
    const bookmarkButton = document.getElementById('bookmark-btn');
    const bookmarked = bookmarkButton.dataset.bookmarked === 'true';
    const bookmarkIcon = document.getElementById('bookmark-icon');
    const bookmarkText = document.getElementById('bookmark-text');
    const newBookmarked = !bookmarked;

    try
    {
        if (newBookmarked)
        {
            await postService.addBookmark(postId);
        } else
        {
            await postService.removeBookmark(postId);
        }

        bookmarkButton.dataset.bookmarked = newBookmarked.toString();
        bookmarkText.textContent = newBookmarked ? '북마크 취소' : '북마크';
        bookmarkIcon.setAttribute('data-lucide', newBookmarked ? 'bookmark-x' : 'bookmark');
        lucide.createIcons();
    }
    catch (error)
    {
        handleError(error);
    }
}

function togglePostMenu(event)
{
    event.stopPropagation();

    const postMenu = document.getElementById('post-menu');
    const isHidden = postMenu.classList.contains('hidden');

    closeAllToggleMenu();

    if (isHidden) postMenu.classList.remove('hidden');
}

function closeAllToggleMenu()
{
    document.querySelectorAll('.toggle-menu')
        .forEach(menu => menu.classList.add('hidden'));
}