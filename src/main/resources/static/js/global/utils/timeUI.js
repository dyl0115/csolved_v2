export function init()
{
    document.querySelectorAll('.timeago')
        .forEach(element =>
        {
            const dateString = element.getAttribute('data-date');
            element.textContent = calculateTimeAgo(dateString);
        });
}

function calculateTimeAgo(dateString)
{
    const elapsedSeconds = Math.floor((new Date() - new Date(dateString)) / 1000);

    const intervals = {
        년: 31536000,
        개월: 2592000,
        일: 86400,
        시간: 3600,
        분: 60
    };

    for (const [unit, secondsInUnit] of Object.entries(intervals))
    {
        const interval = Math.floor(elapsedSeconds / secondsInUnit);

        if (interval >= 1)
        {
            return `${interval}${unit} 전`;
        }
    }

    return '방금 전';
}