import * as noticeClient from '../client/noticeClient.js';

export async function createNotice(form)
{
    const request = {
        categoryId: form.categoryId,
        title: form.title,
        authorId: parseInt(form.authorId),
        anonymous: form.anonymous,
        content: form.content
    };

    return noticeClient.createNotice(request);
}

export async function updateNotice(form)
{
    const request = {
        categoryId: form.categoryId,
        id: parseInt(form.noticeId),
        title: form.title,
        authorId: parseInt(form.authorId),
        anonymous: form.anonymous,
        content: form.content
    };

    return noticeClient.updateNotice(request);
}

export async function deleteNotice(noticeId)
{
    return noticeClient.deleteNotice(noticeId);
}

export async function addLike(noticeId)
{
    return noticeClient.addLike(noticeId);
}