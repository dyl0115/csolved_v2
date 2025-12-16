export function init(selector, onPageChange)
{
    const pagination = document.querySelector(selector);
    if (!pagination) return;

    pagination.querySelector('.previous-btn')
        ?.addEventListener('click', (event) => getPreviousPageContents(event, onPageChange));

    pagination.querySelector('.next-btn')
        ?.addEventListener('click', (event) => getNextPageContents(event, onPageChange));

    pagination.querySelector('.number-input')
        ?.addEventListener('keyup', (event) =>
        {
            if (event.key === 'Enter') getNumberedPageContents(event, onPageChange)
        });
}

function getPreviousPageContents(event, onPageChange)
{
    console.log("이전 페이지 버튼 눌림!");
    const pagination = event.currentTarget.closest('.pagination');
    const currentPage = parseInt(pagination.dataset.currentPage);
    const maxPage = parseInt(pagination.dataset.maxPage);

    if (!validatePageNumber(currentPage - 1, maxPage)) return;
    onPageChange(currentPage - 1);
}

function getNextPageContents(event, onPageChange)
{
    console.log("다음 페이지 버튼 눌림!");
    const pagination = event.currentTarget.closest('.pagination');
    const currentPage = parseInt(pagination.dataset.currentPage);
    const maxPage = parseInt(pagination.dataset.maxPage);

    if (!validatePageNumber(currentPage + 1, maxPage)) return;

    onPageChange(currentPage + 1);
}

function getNumberedPageContents(event, onPageChange)
{
    console.log("페이지 번호 입력창 오키!");
    const pagination = event.currentTarget.closest('.pagination');
    const requiredPage = parseInt(pagination.querySelector('.number-input').value);
    const maxPage = parseInt(pagination.dataset.maxPage);

    if (!validatePageNumber(requiredPage, maxPage)) return;

    onPageChange(requiredPage);
}

function validatePageNumber(pageNumber, maxPage)
{
    return pageNumber >= 1 && pageNumber <= maxPage;
}