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
        console.log('[parseContent] JSON.parse 성공:', delta);

        if (delta?.ops && Array.isArray(delta.ops))
        {
            console.log('[parseContent] delta.ops 반환, 길이:', delta.ops.length);
            return delta.ops;
        } else
        {
            console.log('[parseContent] delta.ops가 없거나 배열이 아님:', delta);
        }
    }
    catch (error)
    {
        console.log('[parseContent] JSON.parse 실패, jsonrepair 시도:', error.message);
        try
        {
            const repaired = jsonrepair(content);
            const delta = JSON.parse(repaired);
            console.log('[parseContent] jsonrepair 성공:', delta);

            if (delta?.ops && Array.isArray(delta.ops))
            {
                console.log('[parseContent] repaired delta.ops 반환, 길이:', delta.ops.length);
                return delta.ops;
            }
        }
        catch (error2)
        {
            console.log('[parseContent] jsonrepair도 실패:', error2.message);
            return null;
        }
    }
    console.log('[parseContent] null 반환 (ops 없음)');
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