export function init()
{
    const previousBtn = document.getElementById('previous-page-btn');
    const nextBtn = document.getElementById('next-page-btn');
    const pageInput = document.getElementById('page-number-input');

    previousBtn?.addEventListener('click', () =>
        getPageContents(previousBtn.value));

    pageInput?.addEventListener('keyup', (event) =>
    {
        if (event.key === 'Enter')
        {
            getPageContents(pageInput.value);
        }
    });

    nextBtn?.addEventListener('click', () =>
    {
        getPageContents(nextBtn.value);
    });
}

function getPageContents(pageNumber)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', pageNumber);
    window.location.href = url.toString();
}