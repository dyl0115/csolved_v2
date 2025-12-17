export async function getBookmarks(page)
{
    console.log("page=" + page);
    const response = await fetch(`/user/activity/bookmark?page=${page}`);

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }

    return response;
}

export async function getMyPosts(page)
{
    const response = await fetch(`/user/activity/myPost?page=${page}`);

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }

    return response;
}

export async function getReplies(page)
{
    const response = await fetch(`/user/activity/replied?page=${page}`);

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }

    return response;
}