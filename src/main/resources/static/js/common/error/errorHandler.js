export function handleError(error)
{
    // if (error.status === 401)
    // {
    //     alert('로그인이 필요합니다.');
    //     window.location.href = '/login'
    // }

    if (error.status === 403)
    {
        alert('권한이 없습니다.');
        return;
    }

    alert(error.message || '오류가 발생했습니다.');
}