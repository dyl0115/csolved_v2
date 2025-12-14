export function init()
{
    const previousBtn = document.getElementById('previous-page-btn');
    const nextBtn = document.getElementById('next-page-btn');
    const pageInput = document.getElementById('page-number-input');

    previousBtn?.addEventListener('click', () =>
        getPagedContents(previousBtn.value));

    nextBtn?.addEventListener('click', () =>
        getPagedContents(nextBtn.value)
    );

    pageInput?.addEventListener('keyup', (event) =>
    {
        if (event.key === 'Enter') getPagedContents(pageInput.value);
    });
}

function getPagedContents(pageNumber)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', pageNumber);
    window.location.href = url.toString();
}