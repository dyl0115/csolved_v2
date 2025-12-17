export function handleError(errorData)
{
    if (errorData.status === 403)
    {
        alert('권한이 없습니다.');
        return;
    }

    alert(errorData.message || '오류가 발생했습니다.');
}
