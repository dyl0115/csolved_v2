const imageUploadUrl = 'http://localhost:8080/api/image';

export async function uploadImage(imageForm)
{
    if (!imageForm) return null;

    const response = await fetch(imageUploadUrl, {
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