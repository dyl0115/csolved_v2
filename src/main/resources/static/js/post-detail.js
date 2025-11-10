// 좋아요 버튼
// async function addLike(postType, postId)
// {
//     const likeButton = document.getElementById("like-button");
//     const likeCountElement = document.getElementById("like-count");
//     let likeCount = parseInt(likeCountElement.textContent, 10) || 0;
//
//     const response = await fetch(`/api/${postType}/${postId}/likes`,
//         {
//             method: "POST",
//             headers: {
//                 "Content-Type": "application/json",
//             },
//         });
//
//     switch (response.status)
//     {
//         case 200:
//             likeCount++;
//             likeCountElement.textContent = likeCount;
//             likeButton.disabled = true;
//             break;
//         case 409:
//             const modal = new bootstrap.Modal(document.getElementById("addLikeConflictModal"));
//             modal.show();
//             const closeModalButton = document.getElementById("closeModalButton");
//             closeModalButton.addEventListener("click", () =>
//             {
//                 modal.hide();
//             });
//             break;
//         default:
//             alert('좋아요 처리 중 오류가 발생했습니다.');
//             break;
//     }
// }

// 북마크 토글 버튼
async function toggleBookmark(bookmarkButton)
{
    const postId = bookmarkButton.getAttribute('postId');
    const bookmarked = bookmarkButton.getAttribute('bookmarked') === 'true';
    const bookmarkIcon = document.getElementById('bookmark-icon');
    const bookmarkText = document.getElementById('bookmark-text');
    const requestUrl = `/api/bookmark/${postId}`;
    const requestMethod = bookmarked ? 'DELETE' : 'POST';

    const response = await fetch(requestUrl,
        {
            method: requestMethod
        })

    switch (response.status)
    {
        case 200:
            const newBookmarked = !bookmarked;
            bookmarkButton.setAttribute('bookmarked', newBookmarked);
            bookmarkText.textContent = newBookmarked ? '북마크 취소' : '북마크';
            bookmarkIcon.setAttribute('data-lucide', newBookmarked ? 'bookmark-x' : 'bookmark');
            // Lucide 아이콘 재초기화
            lucide.createIcons();
            break;
        default:
            alert('북마크 처리 중 오류가 발생했습니다.');
            bookmarkText.textContent = bookmarked ? '북마크 취소' : '북마크';
            break;
    }
}

// 게시글 삭제
// async function deletePost(postType, pluralPostType, postId)
// {
//     try
//     {
//         const response = await fetch(`/api/${postType}/${postId}`, {
//             method: 'DELETE',
//             headers: {
//                 'Content-Type': 'application/json',
//             }
//         });
//
//         if (!response.ok)
//         {
//             throw new Error('삭제에 실패했습니다.');
//         }
//         window.location.replace(`/${pluralPostType}?page=1`);
//
//     }
//     catch (error)
//     {
//         console.error('Error:', error);
//         alert('삭제 중 오류가 발생했습니다.');
//     }
// }

// 답글 삭제
async function deleteAnswer(answerId)
{
    try
    {
        const response = await fetch(`/api/answers/${answerId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok)
        {
            throw new Error('삭제에 실패했습니다.');
        }

        window.location.reload();
    }
    catch (error)
    {
        console.error('Error:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
}

async function deleteComment(commentId)
{
    try
    {
        const response = await fetch(`/api/comments/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok)
        {
            throw new Error('삭제에 실패했습니다.');
        }

        window.location.reload();
    }
    catch (error)
    {
        console.error('Error:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
}

// 댓글 토글 기능
document.getElementById('toggle-answer-form').addEventListener('click', function ()
{
    const card = document.getElementById('answer-form-card');
    card.classList.toggle('hidden');
});

// 대댓글 토글 기능
document.querySelectorAll('.toggle-comment-form').forEach(element =>
{
    element.addEventListener('click', function ()
    {
        const commentForm = this.nextElementSibling;
        commentForm.classList.toggle('hidden');
    });
});

// 이미지 크기 조절
document.addEventListener('DOMContentLoaded', function ()
{
    const postContent = document.querySelector('.post-content-container');
    const images = postContent.querySelectorAll('img');

    images.forEach(img =>
    {
        // 원본 크기 정보 확인 및 저장
        const originalWidth = img.getAttribute('width');
        const originalHeight = img.getAttribute('height');

        // width와 height 속성 제거
        img.removeAttribute('width');
        img.removeAttribute('height');

        // 인라인 스타일로 최대 너비 설정
        img.style.maxWidth = '100%';

        // 작은 이미지의 경우 원래 크기 유지
        if (originalWidth && parseInt(originalWidth) < postContent.offsetWidth)
        {
            img.style.width = originalWidth + 'px';
        } else
        {
            img.style.width = 'auto';
        }

        img.style.height = 'auto';
        img.style.objectFit = 'contain';
    });
});