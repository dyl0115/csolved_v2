let tags = new Set();
let tagContainer, tagInput, tagHiddenInput, tagTemplate;

export function init()
{
    tagContainer = document.getElementById('tags-container');
    tagInput = document.getElementById('tag-input');
    tagHiddenInput = document.getElementById('tag-hidden-input');
    tagTemplate = document.getElementById('tag-template');

    if (!tagContainer || !tagInput || !tagHiddenInput || !tagTemplate) return;

    // 기존 태그 로드 (수정 시)
    if (tagHiddenInput.value)
    {
        tagHiddenInput.value
            .split(',')
            .map(tag => tag.trim())
            .filter(Boolean)
            .forEach(addTag);
    }

    // 엔터키
    tagInput.addEventListener('keydown', (e) =>
    {
        if (e.key === 'Enter')
        {
            e.preventDefault();
            handleTagInput(tagInput.value);
        }
    });

    // 스페이스바
    tagInput.addEventListener('input', (e) =>
    {
        if (e.target.value.includes(' '))
        {
            handleTagInput(e.target.value.replace(' ', ''));
        }
    });

    // 컨테이너 클릭 → input 포커스
    tagContainer.addEventListener('click', (e) =>
    {
        if (e.target === tagContainer)
        {
            tagInput.focus();
        }
    });

    // x버튼 클릭 (이벤트 위임)
    tagContainer.addEventListener('click', (e) =>
    {
        if (e.target.matches('.tag-badge button'))
        {
            const tag = e.target.closest('.tag-badge');
            tags.delete(tag.dataset.tag);
            tag.remove();
            updateHiddenInput();
        }
    });
}

function handleTagInput(text)
{
    text = text.trim();
    if (!isValidTag(text)) return;

    addTag(text);
    tagInput.value = '';
}

function isValidTag(text)
{
    if (text === '') return false;
    if (tags.has(text))
    {
        alert('이미 존재하는 태그입니다.');
        return false;
    }
    return true;
}

function addTag(tagText)
{
    tags.add(tagText);

    const clone = tagTemplate.content.cloneNode(true);
    const badge = clone.querySelector('.tag-badge');

    badge.dataset.tag = tagText;
    badge.querySelector('.tag-text').textContent = tagText;

    tagContainer.insertBefore(badge, tagInput);
    updateHiddenInput();
}

function updateHiddenInput()
{
    tagHiddenInput.value = [...tags].join(',');
}