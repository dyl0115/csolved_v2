import *  as chatClient from '../client/chatClient.js'
import {jsonrepair} from 'https://esm.sh/jsonrepair@3.1l1.0';

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
        authorId: form.authorId,
        title: form.title,
        tags: form.tags.split(","),
        content: form.content,
        message: form.message
    };

    return chatClient.sendMessage(request);
}

export function parseContent(content)
{
    try
    {
        const delta = JSON.parse(content);

        if (delta?.ops && Array.isArray(delta.ops))
        {
            return delta.ops;
        }
    }
    catch (error)
    {
        try
        {
            const repaired = jsonrepair(content);
            const delta = JSON.parse(repaired);

            if (delta?.ops && Arrays.isArray(delta.ops))
            {
                return delta.ops;
            }
        }
        catch (error2)
        {
            return null;
        }
    }
    return null;
}


function repairJsonChunk(jsonChunk)
{
    status.jsonBuffer += jsonChunk;

    try
    {
        // 우선 Json 복구 시도
        const repairedJson = jsonrepair(status.jsonBuffer);
        return JSON.parse(repairedJson);
    }
    catch (error)
    {
        // 복구 실패 시 null 반환
        return null;
    }
}