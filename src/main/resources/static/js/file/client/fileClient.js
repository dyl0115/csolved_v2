export async function uploadImage(imageForm)
{
    if (!imageForm) return null;

    const response = await fetch('/api/image', {
        method: 'POST',
        body: imageForm,
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }

    const data = await response.json();
    return data.imageUrl;
}