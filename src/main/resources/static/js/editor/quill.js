export class QuillEditor
{
    constructor(selector)
    {
        this.quill = null;
        this.imageUploadUrl = 'http://localhost:8080/api/image';
        this.init(selector);
    }

    init(selector)
    {
        this.quill = new Quill(selector, {
            theme: 'snow',
            modules: {
                toolbar: {
                    container: [
                        ['bold', 'italic', 'underline'],
                        ['link', 'image', 'video'],
                        [{'list': 'ordered'}, {'list': 'bullet'}]
                    ],
                    handlers: {
                        image: () => this.imageHandler()
                    }
                },
                resize: {
                    modules: ['Resize', 'DisplaySize']
                }
            }
        });

        this.setStyles();
        this.setOriginalContent();
        this.setEvents();
    }

    setStyles()
    {
        this.quill.root.style.minHeight = '300px';
        this.quill.root.spellcheck = false;
    }

    setOriginalContent()
    {
        const originalContent = document.getElementById('content').value;
        if (originalContent)
        {
            this.quill.root.innerHTML = originalContent;
        }
    }

    setEvents()
    {
        this.quill.on('text-change', () =>
        {
            document.getElementById('content').value = this.quill.root.innerHTML;
        });
    }

    async imageHandler()
    {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () =>
        {
            const file = input.files[0];
            if (!file) return;

            const range = this.quill.getSelection();
            this.quill.insertText(range.index, '업로드 중...');

            const formData = new FormData();
            formData.append('image', file);

            try
            {
                const response = await fetch(this.imageUploadUrl, {
                    method: 'POST',
                    body: formData
                })

                const data = await response.json();
                const awsS3Url = data.imageUrl;

                this.quill.deleteText(range.index, 6); // '업로드 중...' 삭제
                this.quill.insertEmbed(range.index, 'image', awsS3Url);
                this.quill.setSelection(range.index + 1);

            }
            catch (error)
            {
                alert('이미지 업로드에 실패했습니다.');
                this.quill.deleteText(range.index, 6);
            }
        }
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