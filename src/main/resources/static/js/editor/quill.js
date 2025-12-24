import * as fileClient from '../file/client/fileClient.js';
import {handleError} from "../global/error/errorHandler.js";

let quill = null;

export function init(selector)
{
    quill = new Quill(selector, {
        theme: 'snow',
        modules: {
            toolbar: {
                container: [
                    ['bold', 'italic', 'underline'],
                    ['link', 'image', 'video'],
                    [{'list': 'ordered'}, {'list': 'bullet'}]
                ],
                handlers: {
                    image: imageHandler
                }
            },
            resize: {
                modules: ['Resize', 'DisplaySize']
            }
        }
    });

    setStyles();
    setOriginalContent();
    setEvents();
}

function setStyles()
{
    quill.root.style.minHeight = '300px';
    quill.root.spellcheck = false;
}

function setOriginalContent()
{
    const originalContent = document.getElementById('content').value;
    if (originalContent)
    {
        quill.root.innerHTML = originalContent;
    }
}

function setEvents()
{
    quill.on('text-change', () =>
    {
        document.getElementById('content').value = quill.root.innerHTML;
    });

    // 붙여넣기 이벤트 처리 (base64 이미지 자동 업로드)
    quill.root.addEventListener('paste', handlePaste);
}

export function getQuill()
{
    return quill;
}

function selectFile(accept)
{
    return new Promise((resolve) =>
    {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = accept;
        input.onchange = () => resolve(input.files[0] || null);
        input.click();
    });
}

function showLoading(index)
{
    quill.insertText(index, '업로드 중...');
}

function hideLoading(index)
{
    quill.deleteText(index, 6);
}

async function uploadFile(file)
{
    const formData = new FormData();
    formData.append('image', file);
    return await fileClient.uploadImage(formData);
}

function insertImage(index, url)
{
    quill.insertEmbed(index, 'image', url);
    quill.setSelection(index + 1);
}

async function imageHandler()
{
    const file = await selectFile('image/*');
    if (!file) return;

    const range = quill.getSelection();

    showLoading(range.index);

    try
    {
        const imageUrl = await uploadFile(file);
        hideLoading(range.index);
        insertImage(range.index, imageUrl);
    }
    catch (error)
    {
        hideLoading(range.index);
        handleError(error);
    }
}

/**
 * 붙여넣기 이벤트 핸들러
 * - 클립보드에서 이미지 파일이 있으면 업로드
 * - HTML에 base64 이미지가 있으면 업로드
 */
async function handlePaste(event)
{
    const clipboardData = event.clipboardData;
    if (!clipboardData) return;

    // 1. 클립보드에 이미지 파일이 있는 경우 (스크린샷, 파일 복사 등)
    const imageFile = getImageFileFromClipboard(clipboardData);
    if (imageFile)
    {
        event.preventDefault();
        await uploadAndInsertImage(imageFile);
        return;
    }

    // 2. HTML에 base64 이미지가 있는 경우 (웹에서 이미지 복사)
    const html = clipboardData.getData('text/html');
    if (html && containsBase64Image(html))
    {
        event.preventDefault();
        await handleBase64ImagePaste(html);
    }
}

/**
 * 클립보드에서 이미지 파일 추출
 */
function getImageFileFromClipboard(clipboardData)
{
    const items = clipboardData.items;
    for (let i = 0; i < items.length; i++)
    {
        if (items[i].type.startsWith('image/'))
        {
            return items[i].getAsFile();
        }
    }
    return null;
}

/**
 * HTML에 base64 이미지가 포함되어 있는지 확인
 */
function containsBase64Image(html)
{
    return html.includes('data:image/');
}

/**
 * base64 이미지가 포함된 HTML 붙여넣기 처리
 */
async function handleBase64ImagePaste(html)
{
    // base64 이미지 추출
    const base64Regex = /data:image\/(png|jpeg|jpg|gif|webp);base64,[A-Za-z0-9+/=]+/g;
    const matches = html.match(base64Regex);

    if (!matches || matches.length === 0) return;

    const range = quill.getSelection() || {index: quill.getLength()};

    for (const base64Data of matches)
    {
        try
        {
            showLoading(range.index);

            // base64 → File 변환
            const file = base64ToFile(base64Data);

            // 업로드
            const imageUrl = await uploadFile(file);

            hideLoading(range.index);
            insertImage(range.index, imageUrl);
        }
        catch (error)
        {
            hideLoading(range.index);
            console.error('base64 이미지 업로드 실패:', error);
            handleError(error);
        }
    }
}

/**
 * 이미지 파일 업로드 및 에디터에 삽입
 */
async function uploadAndInsertImage(file)
{
    const range = quill.getSelection() || {index: quill.getLength()};

    showLoading(range.index);

    try
    {
        const imageUrl = await uploadFile(file);
        hideLoading(range.index);
        insertImage(range.index, imageUrl);
    }
    catch (error)
    {
        hideLoading(range.index);
        handleError(error);
    }
}

/**
 * base64 데이터를 File 객체로 변환
 */
function base64ToFile(base64Data)
{
    // data:image/png;base64,xxxxx 형식 파싱
    const [header, data] = base64Data.split(',');
    const mimeMatch = header.match(/data:(image\/\w+);base64/);
    const mimeType = mimeMatch ? mimeMatch[1] : 'image/png';
    const extension = mimeType.split('/')[1];

    // base64 → binary
    const binaryString = atob(data);
    const bytes = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++)
    {
        bytes[i] = binaryString.charCodeAt(i);
    }

    // File 객체 생성
    const blob = new Blob([bytes], {type: mimeType});
    return new File([blob], `pasted-image-${Date.now()}.${extension}`, {type: mimeType});
}

// function videoHandler()
// {
//     const videoUploadUrl = 'http://localhost:8080/api/video';
//     const input = document.createElement('input');
//     input.setAttribute('type', 'file');
//     input.setAttribute('accept', 'video/*');
//     input.click();
//
//     input.onchange = async () =>
//     {
//         const file = input.files[0];
//
//         if (file)
//         {
//             // 비디오 업로드 중 표시 (선택사항)
//             const range = status.quill.getSelection();
//             status.quill.insertText(range.index, '업로드 중...');
//
//             // FormData 생성
//             const formData = new FormData();
//             formData.append('video', file);
//
//             try
//             {
//                 const response = await fetch(videoUploadUrl, {
//                     method: 'POST',
//                     body: formData
//                 });
//
//                 const data = await response.json();
//                 const awsS3Url = data.videoUrl;
//
//                 alert('awsURl ' + awsS3Url);// AWS S3 URL
//
//                 // '업로드 중...' 텍스트 삭제
//                 status.quill.deleteText(range.index, 12);
//
//                 // 에디터에 비디오 삽입
//                 status.quill.insertEmbed(range.index, 'video', awsS3Url);
//
//                 // 커서를 비디오 다음으로 이동
//                 status.quill.setSelection(range.index + 1);
//
//             }
//             catch (error)
//             {
//                 console.error('비디오 업로드 실패:', error);
//                 alert('비디오 업로드에 실패했습니다.');
//                 // '업로드 중...' 텍스트 삭제
//                 status.quill.deleteText(range.index, 12);
//             }
//         }
//     };
// }