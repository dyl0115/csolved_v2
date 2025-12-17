import * as userService from '../service/userActivityService.js';
import * as paginationUI from '../../global/utils/paginationUI.js';
import * as navigationUI from '/js/global/utils/navigationUI.js';
import * as timeUI from '/js/global/utils/timeUI.js';
import {handleError} from "../../global/error/errorHandler.js";


export function init()
{
    paginationUI.init('#bookmark-pagination', onBookmarkPageChange);
    paginationUI.init('#my-post-pagination', onMyPostPageChange);
    paginationUI.init('#reply-pagination', onReplyPageChange);
    navigationUI.init();
    timeUI.init();

    document.getElementById('bookmarks-tab')
        ?.addEventListener('click', () => switchTab('bookmarks'));

    document.getElementById('posts-tab')
        ?.addEventListener('click', () => switchTab('posts'));

    document.getElementById('comments-tab')
        ?.addEventListener('click', () => switchTab('comments'));
}

async function onBookmarkPageChange(page)
{
    try
    {
        const response = await userService.getBookmarks(page);
        document.getElementById('bookmark-list').outerHTML = await response.text();

        switchTab('bookmarks');
        paginationUI.init('#bookmark-pagination', onBookmarkPageChange);
        document.querySelector('.container').scrollIntoView({behavior: 'smooth'});

        lucide.createIcons();
    }
    catch (error)
    {
        handleError(error);
    }

}

async function onMyPostPageChange(page)
{
    try
    {
        const response = await userService.getMyPosts(page);
        document.getElementById('userPostList').outerHTML = await response.text();

        switchTab('posts');
        paginationUI.init('#my-post-pagination', onMyPostPageChange);
        document.querySelector('.container').scrollIntoView({behavior: 'smooth'});

        lucide.createIcons();
    }
    catch (error)
    {
        handleError(error);
    }
}

async function onReplyPageChange(page)
{
    try
    {
        const response = await userService.getReplies(page);
        document.getElementById('repliedPostList').outerHTML = await response.text();

        switchTab('comments');
        paginationUI.init('#reply-pagination', onReplyPageChange);
        document.querySelector('.container').scrollIntoView({behavior: 'smooth'});

        lucide.createIcons();
    }
    catch (error)
    {
        handleError(error);
    }
}

function switchTab(tabName)
{
    const tabs = document.querySelectorAll('[role="tab"]');
    const tabPanes = document.querySelectorAll('[role="tabpanel"]');

    // 모든 탭 비활성화
    tabs.forEach(tab =>
    {
        tab.classList.remove('border-gray-800', 'bg-gray-50', 'text-gray-800');
        tab.classList.add('border-transparent', 'text-gray-500');
        tab.setAttribute('aria-selected', 'false');
    });

    // 모든 탭 패널 숨기기
    tabPanes.forEach(pane =>
    {
        pane.classList.add('hidden');
        pane.classList.remove('block');
    });

    // 선택된 탭 활성화
    if (tabName === 'bookmarks')
    {
        document.getElementById('bookmarks-tab').classList.remove('border-transparent', 'text-gray-500');
        document.getElementById('bookmarks-tab').classList.add('border-gray-800', 'bg-gray-50', 'text-gray-800');
        document.getElementById('bookmarks-tab').setAttribute('aria-selected', 'true');
        document.getElementById('bookmark-list').classList.remove('hidden');
        document.getElementById('bookmark-list').classList.add('block');
    } else if (tabName === 'posts')
    {
        document.getElementById('posts-tab').classList.remove('border-transparent', 'text-gray-500');
        document.getElementById('posts-tab').classList.add('border-gray-800', 'bg-gray-50', 'text-gray-800');
        document.getElementById('posts-tab').setAttribute('aria-selected', 'true');
        document.getElementById('userPostList').classList.remove('hidden');
        document.getElementById('userPostList').classList.add('block');
    } else if (tabName === 'comments')
    {
        document.getElementById('comments-tab').classList.remove('border-transparent', 'text-gray-500');
        document.getElementById('comments-tab').classList.add('border-gray-800', 'bg-gray-50', 'text-gray-800');
        document.getElementById('comments-tab').setAttribute('aria-selected', 'true');
        document.getElementById('repliedPostList').classList.remove('hidden');
        document.getElementById('repliedPostList').classList.add('block');
    }

    // 아이콘 재초기화
    lucide.createIcons();
}