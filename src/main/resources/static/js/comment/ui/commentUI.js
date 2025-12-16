import * as commentService from '../service/commentService.js';
import {handleError} from "../../global/error/errorHandler.js";

const target = {
    type: null,
}

export function init(targetType)
{
    target.type = targetType;

    document.querySelectorAll('.comment-create-btn')
        .forEach(btn => btn.addEventListener('click', createComment));

    document.querySelectorAll('.toggle-comment-create-btn')
        .forEach(btn => btn.addEventListener('click', toggleCommentCreateForm));

    document.querySelectorAll('.comment-menu-btn')
        .forEach(btn => btn.addEventListener('click', toggleCommentMenu));

    document.querySelectorAll('.comment-delete-btn')
        .forEach(btn => btn.addEventListener('click', deleteComment));
}

async function createComment(event)
{
    const targetId = document.getElementById('post-id').textContent;
    const createForm = event.currentTarget.closest('.comment-create-form');
    const answerId = createForm.querySelector('.comment-create-answer-id').value;
    const authorId = createForm.querySelector('.comment-create-author-id').value;
    const content = createForm.querySelector('.comment-create-content').value;
    const anonymous = createForm.querySelector('.comment-create-anonymous').checked;

    const commentErrorDiv = createForm.querySelector('.comment-create-error');

    const form = {
        postId: targetId,
        answerId: answerId,
        authorId: authorId,
        anonymous: anonymous,
        content: content
    }

    try
    {
        await commentService.createComment(form);
        window.location.href = `/${target.type}/${targetId}?skipView=true`;
    }
    catch (error)
    {
        commentErrorDiv.classList.toggle('hidden');
        commentErrorDiv.textContent = error.message;
    }
}

async function deleteComment(event)
{
    const targetId = document.getElementById('post-id').textContent;
    const commentContainer = event.currentTarget.closest('.comment-container');
    const commentId = commentContainer.dataset.commentId;

    if (confirm('댓글을 삭제하시겠습니까?'))
    {
        try
        {
            await commentService.deleteComment(commentId);
            window.location.href = `/${target.type}/${targetId}?skipView=true`;
        }
        catch (error)
        {
            handleError(error);
        }
    }
}

function toggleCommentMenu(event)
{
    event.stopPropagation();
    const commentMenuContainer = event.currentTarget.closest('.comment-menu-container');
    const commentMenu = commentMenuContainer.querySelector('.comment-menu');
    const isHidden = commentMenu.classList.contains('hidden');

    closeAllToggleMenu();

    if (isHidden) commentMenu.classList.remove('hidden');
}

function closeAllToggleMenu()
{
    document.querySelectorAll('.toggle-menu')
        .forEach(menu => menu.classList.add('hidden'));
}

function toggleCommentCreateForm(event)
{
    const answerContainer = event.currentTarget.closest('.answer-container');
    const commentCreateForm = answerContainer.querySelector('.comment-create-form');
    const isHidden = commentCreateForm.classList.contains('hidden');

    closeAllToggleForm();

    if (isHidden) commentCreateForm.classList.remove('hidden');
}

function closeAllToggleForm()
{
    document.querySelectorAll('.toggle-form')
        .forEach(form => form.classList.add('hidden'));
}