import * as userClient from '../client/userClient.js';

export async function getBookmarks(page)
{
    return userClient.getBookmarks(page);
}

export async function getMyPosts(page)
{
    return userClient.getMyPosts(page);
}

export async function getReplies(page)
{
    return userClient.getReplies(page);
}