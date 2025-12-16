import * as navigationUI from '/js/global/utils/navigationUI.js';
import * as paginationUI from '/js/global/utils/paginationUI.js';
import * as sortUI from '/js/global/utils/sortUI.js';
import * as timeUI from '/js/global/utils/timeUI.js';

export function init()
{
    navigationUI.init();
    paginationUI.init('#notice-pagination', onPageChange);
    sortUI.init();
    timeUI.init();
}

function onPageChange(requiredPage)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', requiredPage);
    window.location.href = url;
}

