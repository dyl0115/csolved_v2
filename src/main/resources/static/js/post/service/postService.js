import * as postClient from '../client/postClient.js'

export async function createPost(form)
{
    const request = {
        categoryId: parseInt(form.categoryId),
        title: form.title,
        authorId: parseInt(form.authorId),
        anonymous: form.anonymous,
        tags: form.tags,
        content: form.content
    }

    return postClient.createPost(request);
}

export async function updatePost(form)
{
    const request = {
        id: parseInt(form.postId),
        categoryId: parseInt(form.categoryId),
        title: form.title,
        authorId: parseInt(form.authorId),
        anonymous: form.anonymous,
        tags: form.tags,
        content: form.content
    };

    return postClient.updatePost(request);
}

export async function deletePost(postId)
{
    return postClient.deletePost(postId);
}

export async function addPostLike(postId)
{
    return postClient.addPostLike(postId);
}

export async function addBookmark(postId)
{
    return postClient.addBookmark(postId);
}

export async function removeBookmark(postId)
{
    return postClient.removeBookmark(postId);
}