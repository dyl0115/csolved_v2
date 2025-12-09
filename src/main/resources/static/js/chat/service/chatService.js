import *  as chatClient from '../client/chatClient.js'

const status = {
    jsonBuffer: '',
}

export function handleStream(updateViews)
{
    chatClient.handleStream((jsonChunk) =>
    {
        const post = repairJsonChunk(jsonChunk);
        if (post != null) updateViews(post);
    })
}

export function sendMessage(form)
{
    status.jsonBuffer = '';

    const request = {
        role: "user",
        title: form.title,
        content: form.content,
        tags: form.tags.split(","),
        message: form.message
    };

    return chatClient.sendMessage(request);
}


function repairJsonChunk(jsonChunk)
{
    status.jsonBuffer += jsonChunk;

    try
    {
        // 우선 Json 복구 시도
        const repairedJson = window.jsonRepair(status.jsonBuffer);
        return JSON.parse(repairedJson);
    }
    catch (error)
    {
        // 복구 실패 시 null 반환
        return null;
    }
}