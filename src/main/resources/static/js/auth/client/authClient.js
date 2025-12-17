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

export async function withdraw(request)
{
    const response = await fetch('/api/auth/withdraw', {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        throw await response.json();
    }
}

export async function updatePassword(request)
{
    const response = await fetch('/api/auth/password',
        {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(request)
        })

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}