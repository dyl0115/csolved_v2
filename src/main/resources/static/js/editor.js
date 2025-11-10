const scriptEl = document.currentScript;
const uploadUrl = scriptEl.dataset.uploadImageUrl;
const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);

function initEditor()
{
    return new Promise((resolve) =>
    {
        tinymce.init({
            selector: '.large-editor',
            height: 500,
            plugins: 'code codesample link image table lists advlist autolink',
            menubar: false,
            toolbar: 'undo redo | blocks | bold italic | alignleft aligncenter alignright | indent outdent | bullist numlist | codesample | link image',
            codesample_languages: [
                {text: 'HTML/XML', value: 'markup'},
                {text: 'JavaScript', value: 'javascript'},
                {text: 'CSS', value: 'css'},
                {text: 'PHP', value: 'php'},
                {text: 'Python', value: 'python'},
                {text: 'Java', value: 'java'},
                {text: 'SQL', value: 'sql'}
            ],
            mobile: {
                menubar: false,
                toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | codesample | link image',
                height: '450px'
            },
            codesample_global_prismjs: true,
            forced_root_block: 'p',
            remove_trailing_brs: false,
            br_in_pre: false,
            keep_styles: true,
            entity_encoding: 'raw',
            paste_enable_default_filters: false,
            paste_word_valid_elements: '*[*]',
            paste_webkit_styles: 'all',
            paste_merge_formats: true,
            browser_spellcheck: true,

            // 코드블록 하이라이팅 이후, 한글 입력이 안되는 문제 해결
            input_ime: true,
            toolbar_sticky: false,
            invalid_elements: '',
            extended_valid_elements: '*[*]',

            // 이미지 업로드 설정
            images_upload_url: uploadUrl || '',
            images_upload_handler: function (blobInfo, progress)
            {
                return new Promise(async (resolve, reject) =>
                {
                    try
                    {
                        // 1. 이미지 압축
                        const compressedBlob = await new Promise((resolve, reject) =>
                        {
                            new Compressor(blobInfo.blob(), {
                                quality: 0.6, // 화질 (0 ~ 1)
                                maxWidth: 1920, // 최대 너비
                                maxHeight: 1080, // 최대 높이
                                mimeType: 'image/jpeg', // JPEG로 변환 (용량 70% 감소)
                                convertSize: 1000000, // 1MB 이상 이미지만 변환
                                success(result)
                                {
                                    resolve(result);
                                },
                                error(err)
                                {
                                    reject(err);
                                }
                            });
                        });

                        // 2. 압축된 이미지 업로드
                        const xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', uploadUrl);

                        xhr.upload.onprogress = (e) =>
                        {
                            progress(e.loaded / e.total * 100);
                        };

                        xhr.onload = () =>
                        {
                            if (xhr.status < 200 || xhr.status >= 300)
                            {
                                reject('HTTP Error: ' + xhr.status);
                                return;
                            }
                            const json = JSON.parse(xhr.responseText);
                            resolve(json.location);
                        };

                        xhr.onerror = () =>
                        {
                            reject('Upload failed');
                        };

                        const formData = new FormData();
                        formData.append('file', compressedBlob, blobInfo.filename()); // 압축된 Blob 사용
                        xhr.send(formData);

                    }
                    catch (err)
                    {
                        reject('Compression failed: ' + err.message);
                    }
                });
            },

            content_style: `
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
                    padding: 10px;
                }
                .mce-content-body pre[class*="language-"] {
                    margin: 15px 0;
                }
                .mce-content-body code[class*="language-"] {
                    font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
                }
                .mce-content-body.is-invalid { border: 1px solid red; 
                }
                p {
                    margin: 0;
                    padding: 8px 0;
                    min-height: 20px;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
            `,
            setup: function (editor)
            {
                editor.on('change keyup', function ()
                {
                    Prism.highlightAll();
                });

                editor.on('init', function ()
                {
                    // 현재 에디터의 ID를 가져옴
                    const editorId = editor.id;
                    const editorElement = document.getElementById(editorId);

                    if (editorElement && editorElement.classList.contains('is-invalid'))
                    {
                        editor.getContainer().style.border = '1px solid #dc3545';
                    }
                });
            },
            images_reuse_filename: true, // 원본 파일명 재사용
            images_upload_credentials: true, // 쿠키 전송 허용
        });

        tinymce.init({
            selector: '.medium-editor',
            height: 300,
            width: "100%",
            plugins: 'code codesample table lists advlist autolink',
            menubar: false,
            toolbar: 'undo redo | blocks | bold italic | alignleft aligncenter alignright | indent outdent | bullist numlist | codesample',
            codesample_languages: [
                {text: 'HTML/XML', value: 'markup'},
                {text: 'JavaScript', value: 'javascript'},
                {text: 'CSS', value: 'css'},
                {text: 'PHP', value: 'php'},
                {text: 'Python', value: 'python'},
                {text: 'Java', value: 'java'},
                {text: 'SQL', value: 'sql'}
            ],
            mobile: {
                menubar: false,
                toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist | codesample | link image',
                height: '300px'
            },
            codesample_global_prismjs: true,
            forced_root_block: 'p',
            remove_trailing_brs: false,
            br_in_pre: false,

            keep_styles: true,
            entity_encoding: 'raw',
            paste_enable_default_filters: false,
            paste_word_valid_elements: '*[*]',
            paste_webkit_styles: 'all',
            paste_merge_formats: true,
            browser_spellcheck: true,

            // 코드블록 하이라이팅 이후, 한글 입력이 안되는 문제 해결
            input_ime: true,
            toolbar_sticky: false,
            invalid_elements: '',
            extended_valid_elements: '*[*]',

            content_style: `
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
                    padding: 10px;
                }
                .mce-content-body pre[class*="language-"] {
                    margin: 15px 0;
                }
                .mce-content-body code[class*="language-"] {
                    font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
                }
                .mce-content-body.is-invalid { border: 1px solid red; 
                }
                p {
                    margin: 0;
                    padding: 8px 0;
                    min-height: 20px;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
            `,
            setup: function (editor)
            {
                editor.on('change keyup', function ()
                {
                    Prism.highlightAll();
                });

                editor.on('init', function ()
                {
                    if (document.querySelector('.medium-editor').classList.contains('is-invalid'))
                    {
                        editor.getContainer().style.border = '1px solid #dc3545';
                    }
                });
            },
        });

        tinymce.init({
            selector: '.small-editor',
            height: 250,
            width: "100%",
            plugins: 'table lists advlist autolink',
            menubar: false,
            toolbar: 'undo redo | blocks | bold italic | alignleft aligncenter alignright | indent outdent | bullist numlist | codesample',
            codesample_languages: [
                {text: 'HTML/XML', value: 'markup'},
                {text: 'JavaScript', value: 'javascript'},
                {text: 'CSS', value: 'css'},
                {text: 'PHP', value: 'php'},
                {text: 'Python', value: 'python'},
                {text: 'Java', value: 'java'},
                {text: 'SQL', value: 'sql'}
            ],
            codesample_global_prismjs: true,
            forced_root_block: 'p',
            remove_trailing_brs: false,
            br_in_pre: false,
            keep_styles: true,
            entity_encoding: 'raw',
            paste_enable_default_filters: false,
            paste_word_valid_elements: '*[*]',
            paste_webkit_styles: 'all',
            paste_merge_formats: true,
            browser_spellcheck: true,

            // 코드블록 하이라이팅 이후, 한글 입력이 안되는 문제 해결
            input_ime: true,
            toolbar_sticky: false,
            invalid_elements: '',
            extended_valid_elements: '*[*]',

            // TinyMCE 커스텀 CSS
            content_style: `
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
                    padding: 10px;
                }
                .mce-content-body pre[class*="language-"] {
                    margin: 15px 0;
                }
                .mce-content-body code[class*="language-"] {
                    font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
                }
                .mce-content-body.is-invalid { border: 1px solid red; 
                }
                p {
                    margin: 0;
                    padding: 8px 0;
                    min-height: 20px;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
            `,
            setup: function (editor)
            {
                editor.on('change keyup', function ()
                {
                    Prism.highlightAll();
                });

                editor.on('init', function ()
                {
                    // 현재 에디터의 ID를 가져옴
                    const editorId = editor.id;
                    const editorElement = document.getElementById(editorId);

                    if (editorElement && editorElement.classList.contains('is-invalid'))
                    {
                        editor.getContainer().style.border = '1px solid #dc3545';
                    }
                });
            },
        });
    });
}

document.querySelectorAll('form').forEach(form =>
{
    form.addEventListener('submit', function (e)
    {
        e.preventDefault();
        const textarea = this.querySelector('textarea');
        const editorInstance = tinymce.get(textarea.id);
        textarea.value = editorInstance.getContent();
        this.submit();
    });
});

// DOM이 완전히 로드된 후 초기화
document.addEventListener('DOMContentLoaded', async function ()
{
    try
    {
        await initEditor();
        console.log('Editor initialized successfully');
    }
    catch (error)
    {
        console.error('Editor initialization failed:', error);
        // 폴백: 일반 textarea로 대체
        document.querySelector('.large-editor').style.display = 'block';
    }
});