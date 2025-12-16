import * as commentClient from '../client/commentClient.js';

export async function createComment(form)
{
    const request = {
        postId: parseInt(form.postId),
        answerId: parseInt(form.answerId),
        authorId: parseInt(form.authorId),
        content: form.content,
        anonymous: form.anonymous
    }

    return commentClient.createComment(request);
}

export async function deleteComment(commentId)
{
    return commentClient.deleteComment(commentId);
}