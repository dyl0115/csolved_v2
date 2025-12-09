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