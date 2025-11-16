class ImageProcessor
{
    constructor(maxSize = 256, quality = 0.8)
    {
        this.maxSize = maxSize;
        this.quality = quality;
    }

    async processImage(file)
    {
        try
        {
            const resizedBlob = await this.resize(file);
            const optimizedImage = await this.compress(resizedBlob);
            return optimizedImage;
        }
        catch (error)
        {
            console.error('이미지 처리 중 오류 발생:', error);
            throw error;
        }
    }

    resize(file)
    {
        return new Promise((resolve) =>
        {
            const reader = new FileReader();
            reader.onload = (e) =>
            {
                const img = new Image();
                img.onload = () =>
                {
                    const {width, height} = this.calculateDimensions(img);
                    const canvas = document.createElement('canvas');
                    canvas.width = width;
                    canvas.height = height;

                    const ctx = canvas.getContext('2d');
                    ctx.drawImage(img, 0, 0, width, height);

                    canvas.toBlob(resolve, 'image/jpeg');
                };
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);
        });
    }

    calculateDimensions(img)
    {
        let {width, height} = img;
        if (width > height)
        {
            if (width > this.maxSize)
            {
                height *= this.maxSize / width;
                width = this.maxSize;
            }
        } else
        {
            if (height > this.maxSize)
            {
                width *= this.maxSize / height;
                height = this.maxSize;
            }
        }
        return {width: Math.round(width), height: Math.round(height)};
    }

    compress(blob)
    {
        return new Promise((resolve) =>
        {
            const img = new Image();
            img.onload = () =>
            {
                const canvas = document.createElement('canvas');
                canvas.width = img.width;
                canvas.height = img.height;

                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0);

                canvas.toBlob((compressedBlob) =>
                {
                    resolve({
                        blob: compressedBlob,
                        dataUrl: canvas.toDataURL('image/jpeg', this.quality)
                    });
                }, 'image/jpeg', this.quality);

                URL.revokeObjectURL(img.src);
            };
            img.src = URL.createObjectURL(blob);
        });
    }
}

async function handleImageSelect(input)
{
    if (input.files && input.files[0])
    {
        try
        {
            const processor = new ImageProcessor();
            const result = await processor.processImage(input.files[0]);

            // 미리보기 업데이트
            const imgElement = input.closest('.profile-image-container').querySelector('img');
            imgElement.src = result.dataUrl;

            // 파일 업데이트
            const newFile = new File([result.blob], input.files[0].name, {
                type: 'image/jpeg',
                lastModified: Date.now()
            });

            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(newFile);
            input.files = dataTransfer.files;

        }
        catch (error)
        {
            console.error('이미지 처리 실패:', error);
            alert('이미지 처리 중 오류가 발생했습니다.');
        }
    }
}

function updateProfile()
{
    // form 데이터 가져오기
    const form = document.querySelector('form');
    const formData = new FormData(form);

    // AJAX로 form 제출
    fetch(form.action, {
        method: 'POST',
        body: formData
    })
        .then(response =>
        {
            if (response.ok)
            {
                // 성공 시 모달 표시
                const modal = new bootstrap.Modal(document.getElementById('profileUpdateConfirmModal'));
                modal.show();
            }
        })
        .catch(error =>
        {
            console.error('Error:', error);
        });
}
