const status = {
    eventSource: null,
}

export function handleStream(chunkHandler, errorHandler)
{
    if (status.eventSource)
    {
        console.warn('이미 연결되어 있습니다.')
        return;
    }

    status.eventSource = new EventSource('/ai/post/connect');

    status.eventSource.addEventListener('message', (event) =>
    {
        // console.log('chatClient: ' + event.data);
        chunkHandler(event.data);
    });

    status.eventSource.onerror = (error) =>
    {
        console.error('SSE 연결 오류:', error);
        closeConnection();
        if (errorHandler)
        {
            errorHandler(error);
        }
    }
}

export function closeConnection()
{
    if (status.eventSource)
    {
        status.eventSource.close();
        status.eventSource = null;
        console.log('SSE 연결 종료');
    }
}

export async function sendMessage(request)
{
    const response = await fetch('/ai/post/message', {
        method: "POST",
        headers:
            {
                "Content-Type": "application/json"
            },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message || '알 수 없는 오류');
    }
}

window.addEventListener('beforeunload', () =>
{
    closeConnection();
});