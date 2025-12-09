let eventSource;

export function connect()
{
    if (eventSource == null)
    {
        eventSource = new EventSource('/ai/post/connect');
    }

    eventSource.addEventListener('message', (event) =>
    {
        여기에 비즈니스 로직들이 들어가야 하나.?
    });
}