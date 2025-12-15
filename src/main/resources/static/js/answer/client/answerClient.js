export async function createAnswer(request)
{
    const response = await fetch('/api/answer', {
        method: 'POST',
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

export async function deleteAnswer(answerId)
{
    const response = await fetch(`/api/answer/${answerId}`, {
        method: 'DELETE',
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}

export async function addLike(answerId)
{
    const response = await fetch(`/api/answer/${answerId}/likes`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}