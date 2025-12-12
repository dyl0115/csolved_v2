import Quill from 'https://cdn.jsdelivr.net/npm/quill@2.0.3/+esm';

// function imageHandler()
// {
//     const imageUploadUrl = 'http://localhost:8080/api/image';
//     const input = document.createElement('input');
//     input.setAttribute('type', 'file');
//     input.setAttribute('accept', 'image/*');
//     input.click();
//
//     input.onchange = async () =>
//     {
//         const file = input.files[0];
//
//         if (file)
//         {
//             // 이미지 업로드 중 표시 (선택사항)
//             const range = status.quill.getSelection();
//             status.quill.insertText(range.index, '업로드 중...');
//
//             // FormData 생성
//             const formData = new FormData();
//             formData.append('image', file);  // MultipartFile로 받을 때 키 이름
//
//             try
//             {
//                 const response = await fetch(imageUploadUrl, {
//                     method: 'POST',
//                     body: formData
//                 });
//
//                 const data = await response.json();
//                 const awsS3Url = data.imageUrl;
//
//                 // '업로드 중...' 텍스트 삭제
//                 status.quill.deleteText(range.index, 12);
//
//                 // 에디터에 이미지 삽입
//                 status.quill.insertEmbed(range.index, 'image', awsS3Url);
//
//                 // 커서를 이미지 다음으로 이동
//                 status.quill.setSelection(range.index + 1);
//
//             }
//             catch (error)
//             {
//                 console.error('이미지 업로드 실패:', error);
//                 alert('이미지 업로드에 실패했습니다.');
//                 // '업로드 중...' 텍스트 삭제
//                 status.quill.deleteText(range.index, 12);
//             }
//         }
//     };
// }

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

const QUILL_CONFIG = {
    theme: 'snow',
    modules: {
        toolbar: {
            container: [
                ['bold', 'italic', 'underline'],
                ['link', 'image', 'video'],
                [{'list': 'ordered'}, {'list': 'bullet'}]
            ],
            handlers: {}
        }
    }
};

const EDITOR_STYLE = {
    minHeight: '300px',
    spellcheck: false,
};

export function createEditor(selector)
{
    const quill = new Quill(selector, QUILL_CONFIG);
    setEditorStyles(quill);
    setOriginalContent(quill);
    setEvents(quill);
    return quill;
}

function setEditorStyles(quill)
{
    quill.root.style.minHeight = EDITOR_STYLE.minHeight;
    quill.root.spellcheck = EDITOR_STYLE.spellcheck;
}

function setOriginalContent(quill)
{
    const originalContent = document.getElementById('content').value;
    if (originalContent) quill.root.innerHTML = originalContent;
}

function setEvents(quill)
{
    quill.on('text-change', function ()
    {
        document.getElementById('content').value = quill.root.innerHTML;
    });
}


