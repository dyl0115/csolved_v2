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