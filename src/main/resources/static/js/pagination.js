function goToPage(pageNumber)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', pageNumber);
    window.location.href = url.toString();
}