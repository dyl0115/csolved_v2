import * as answerClient from '../client/answerClient.js';

export async function createAnswer(form)
{
    const request = {
        postId: parseInt(form.postId),
        authorId: parseInt(form.answerAuthorId),
        anonymous: form.answerAnonymous,
        content: form.answerContent,
    }

    return answerClient.createAnswer(request);
}

export async function deleteAnswer(answerId)
{
    return answerClient.deleteAnswer(answerId);
}

export async function addLike(answerId)
{
    return answerClient.addLike(answerId);
}