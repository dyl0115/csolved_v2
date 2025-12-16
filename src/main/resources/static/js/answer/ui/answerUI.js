import * as answerService from '../service/answerService.js';
import {handleError} from "../../global/error/errorHandler.js";

const target = {
    type: null,
}

export function init(targetType)
{
    target.type = targetType;

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
    const answerCreateForm = document.getElementById('answer-create-form');
    const isHidden = answerCreateForm.classList.contains('hidden');

    closeAllToggleForm();

    if (isHidden) answerCreateForm.classList.remove('hidden');
}

function closeAllToggleForm()
{
    document.querySelectorAll('.toggle-form')
        .forEach(form => form.classList.add('hidden'));
}

function toggleAnswerMenu(event)
{
    event.stopPropagation();

    const menuContainer = event.currentTarget.closest('.answer-menu-container');
    const menu = menuContainer.querySelector('.answer-menu');
    const isHidden = menu.classList.contains('hidden');

    closeAllToggleMenu();

    if (isHidden) menu.classList.remove('hidden');
}

function closeAllToggleMenu()
{
    document.querySelectorAll('.toggle-menu')
        .forEach(menu => menu.classList.add('hidden'));
}

async function createAnswer()
{
    const targetId = document.getElementById('post-id').textContent;
    const answerError = document.getElementById('answer-error');

    const form = {
        postId: targetId,
        answerAuthorId: document.getElementById('answer-author-id').value,
        answerAnonymous: document.getElementById('answer-anonymous').checked,
        answerContent: document.getElementById('answer-content').value
    }

    try
    {
        await answerService.createAnswer(form);
        window.location.href = `/${target.type}/${targetId}?skipView=true`;
    }
    catch (error)
    {
        answerError.classList.toggle('hidden');
        answerError.textContent = error.message;
    }
}

async function deleteAnswer(event)
{
    const targetId = document.getElementById('post-id').textContent;
    const answerContainer = event.currentTarget.closest('.answer-container');
    const answerId = answerContainer.dataset.answerId;

    if (confirm('댓글을 삭제하시겠습니까?'))
    {
        try
        {
            await answerService.deleteAnswer(answerId);
            window.location.href = `/${target.type}/${targetId}?skipView=true`;
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