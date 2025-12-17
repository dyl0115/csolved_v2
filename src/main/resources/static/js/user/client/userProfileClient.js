export async function updateProfile(request)
{
    const response = await fetch(`/users/profile`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}