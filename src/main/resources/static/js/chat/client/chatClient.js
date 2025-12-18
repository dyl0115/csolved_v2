const status = {
    eventSource: null,
}

export function handleStream(chunkHandler)
{
    if (status.eventSource) return;

    status.eventSource = new EventSource('/ai/post/connect');

    status.eventSource.addEventListener('message', (event) =>
    {
        // console.log('chatClient: ' + event.data);
        chunkHandler(event.data);
    });
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