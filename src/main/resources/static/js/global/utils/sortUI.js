export function init()
{
    document.getElementById('sort-select')
        ?.addEventListener('change', (event) =>
            getSortedContents(event.target.value));
}

function getSortedContents(sortType)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', 1);
    url.searchParams.set('sortType', sortType);
    window.location.href = url.href;
}