function imageHandler()
{
    const imageUploadUrl = 'http://localhost:8080/api/image';
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.onchange = async () =>
    {
        const file = input.files[0];

        if (file)
        {
            // 이미지 업로드 중 표시 (선택사항)
            const range = quill.getSelection();
            quill.insertText(range.index, '업로드 중...');

            // FormData 생성
            const formData = new FormData();
            formData.append('image', file);  // MultipartFile로 받을 때 키 이름

            try
            {
                const response = await fetch(imageUploadUrl, {
                    method: 'POST',
                    body: formData
                });

                const data = await response.json();
                const awsS3Url = data.imageUrl;

                // '업로드 중...' 텍스트 삭제
                quill.deleteText(range.index, 12);

                // 에디터에 이미지 삽입
                quill.insertEmbed(range.index, 'image', awsS3Url);

                // 커서를 이미지 다음으로 이동
                quill.setSelection(range.index + 1);

            }
            catch (error)
            {
                console.error('이미지 업로드 실패:', error);
                alert('이미지 업로드에 실패했습니다.');
                // '업로드 중...' 텍스트 삭제
                quill.deleteText(range.index, 12);
            }
        }
    };
}

function videoHandler()
{
    const videoUploadUrl = 'http://localhost:8080/api/video';
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'video/*');
    input.click();

    input.onchange = async () =>
    {
        const file = input.files[0];

        if (file)
        {
            // 비디오 업로드 중 표시 (선택사항)
            const range = quill.getSelection();
            quill.insertText(range.index, '업로드 중...');

            // FormData 생성
            const formData = new FormData();
            formData.append('video', file);

            try
            {
                const response = await fetch(videoUploadUrl, {
                    method: 'POST',
                    body: formData
                });

                const data = await response.json();
                const awsS3Url = data.videoUrl;

                alert('awsURl ' + awsS3Url);// AWS S3 URL

                // '업로드 중...' 텍스트 삭제
                quill.deleteText(range.index, 12);

                // 에디터에 비디오 삽입
                quill.insertEmbed(range.index, 'video', awsS3Url);

                // 커서를 비디오 다음으로 이동
                quill.setSelection(range.index + 1);

            }
            catch (error)
            {
                console.error('비디오 업로드 실패:', error);
                alert('비디오 업로드에 실패했습니다.');
                // '업로드 중...' 텍스트 삭제
                quill.deleteText(range.index, 12);
            }
        }
    };
}

function initQuillEditor(selector)
{
    const config = {
        theme: 'snow',
        placeholder: '내용을 입력하세요...',
        modules: {
            toolbar: {
                container: [
                    ['bold', 'italic', 'underline'],
                    ['link', 'image', 'video'],
                    [{'list': 'ordered'}, {'list': 'bullet'}]
                ],
                handlers: {
                    image: imageHandler,
                    video: videoHandler
                }
            },
            resize: {
                modules: ['DisplaySize', 'Toolbar', 'Resize', 'Keyboard'],
            }
        }
    }

    const quill = new Quill(selector, config);
    quill.root.style.minHeight = '300px';
    quill.root.setAttribute("spellcheck", 'false');
    quill.format('size', '15px');

    const originalContent = document.getElementById('content').value;

    if (originalContent)
    {
        quill.root.innerHTML = originalContent;
    }

    quill.on('text-change', function ()
    {
        document.getElementById('content').value = quill.root.innerHTML;
    });
}

