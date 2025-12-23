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

export function addTag(tagText)
{
    tags.add(tagText);

    const clone = tagTemplate.content.cloneNode(true);
    const badge = clone.querySelector('.tag-badge');

    badge.dataset.tag = tagText;
    badge.querySelector('.tag-text').textContent = tagText;

    tagContainer.insertBefore(badge, tagInput);
    updateHiddenInput();
}

export function removeTag(tagText)
{
    tags.delete(tagText);
    
    // UI에서 해당 태그 찾아서 제거
    const badge = tagContainer.querySelector(`[data-tag="${tagText}"]`);
    if (badge) {
        badge.remove();
    }
    
    updateHiddenInput();
}

export function clearAllTags()
{
    // 모든 태그 배지 제거 (input 제외)
    tagContainer.querySelectorAll('.tag-badge').forEach(badge => badge.remove());
    tags.clear();
    updateHiddenInput();
}

function updateHiddenInput()
{
    tagHiddenInput.value = [...tags].join(',');
}

export function getTags()
{
    return [...tags];
}