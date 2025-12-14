export async function createPost(request)
{
    const response = await fetch('/api/post',
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(request)
        });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}

export async function updatePost(request)
{
    const response = await fetch('/api/post', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}