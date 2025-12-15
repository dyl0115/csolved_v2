import * as answerService from '../service/answerService.js';
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    document.getElementById('answer-create-btn')
        ?.addEventListener('click', createAnswer);

    document.querySelectorAll('.answer-delete-btn')
        ?.forEach(btn => btn.addEventListener('click', deleteAnswer));

    document.querySelectorAll('.answer-like-btn')
        ?.forEach(btn => btn.addEventListener('click', addAnswerLike));

    document.getElementById('toggle-answer-create-form')
        ?.addEventListener('click', toggleAnswerCreateForm)

    document.querySelectorAll('.answer-menu-btn')
        ?.forEach(btn => btn.addEventListener('click', toggleAnswerMenu))

    document?.addEventListener('click', closeAllToggleMenu);
}

function toggleAnswerCreateForm()
{
    const form = document.getElementById('answer-create-form');
    const isHidden = form.classList.contains('hidden');

    closeAllToggleForm();

    // 원래 닫혀있었으면 열기
    if (isHidden) form.classList.remove('hidden');
}

function closeAllToggleForm()
{
    document.querySelectorAll('.toggle-form')
        .forEach(form => form.classList.add('hidden'));
}

function toggleAnswerMenu(event)
{
    event.stopPropagation();

    closeAllToggleMenu();
    const menuContainer = event.currentTarget.closest('.answer-menu-container');
    const menu = menuContainer.querySelector('.answer-menu');
    menu.classList.toggle('hidden');
}

function closeAllToggleMenu()
{
    document.querySelectorAll('.toggle-menu')
        .forEach(menu => menu.classList.add('hidden'));
}

async function createAnswer()
{
    const postId = document.getElementById('postId').textContent;
    const answerError = document.getElementById('answer-error');

    const form = {
        postId: document.getElementById('postId').textContent,
        answerAuthorId: document.getElementById('answer-author-id').value,
        answerAnonymous: document.getElementById('answer-anonymous').checked,
        answerContent: document.getElementById('answer-content').value
    }

    try
    {
        await answerService.createAnswer(form);
        window.location.href = `/post/${postId}?skipView=true`;
    }
    catch (error)
    {
        answerError.classList.toggle('hidden');
        answerError.textContent = error.message;
    }
}

async function deleteAnswer(event)
{
    const postId = document.getElementById('postId').textContent;
    const answerContainer = event.currentTarget.closest('.answer-container');
    const answerId = answerContainer.dataset.answerId;

    if (confirm('댓글을 삭제하시겠습니까?'))
    {
        try
        {
            await answerService.deleteAnswer(answerId);
            window.location.href = `/post/${postId}?skipView=true`;
        }
        catch (error)
        {
            handleError(error);
        }
    }
}

async function addAnswerLike(event)
{
    const likeButton = event.currentTarget;
    const answerId = likeButton.dataset.answerId;
    const likeCountSpan = likeButton.querySelector('.answer-like-count');
    const likeInt = parseInt(likeCountSpan.textContent);

    try
    {
        await answerService.addLike(answerId);
        likeCountSpan.textContent = likeInt + 1;
    }
    catch (error)
    {
        handleError(error);
    }
}