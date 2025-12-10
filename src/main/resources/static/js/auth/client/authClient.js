export async function signOut()
{
    const response = await fetch('/api/auth/signOut', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}

export async function withdraw()
{
    const response = await fetch('/api/auth/withdraw', {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'}
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}