function calculateTimeAgo(dateString)
{
    const seconds = Math.floor((new Date() - new Date(dateString)) / 1000);

    // 시간 간격 정의
    const intervals = {
        년: 31536000,
        개월: 2592000,
        일: 86400,
        시간: 3600,
        분: 60
    };

    // 각 간격별로 계산
    for (const [unit, secondsInUnit] of Object.entries(intervals))
    {
        const interval = Math.floor(seconds / secondsInUnit);

        if (interval >= 1)
        {
            return `${interval}${unit} 전`;
        }
    }

    // 1분 미만일 경우
    return '방금 전';
}

// 페이지 내의 모든 시간 표시 요소 업데이트
function updateAllTimeAgos()
{
    const timeElements = document.querySelectorAll('.timeago');

    timeElements.forEach(element =>
    {
        const dateString = element.getAttribute('data-date');
        element.textContent = calculateTimeAgo(dateString);
    });
}

// 1분마다 모든 시간 표시 업데이트
setInterval(updateAllTimeAgos, 60000);

// 페이지 로드 시 초기 업데이트
document.addEventListener('DOMContentLoaded', updateAllTimeAgos);
