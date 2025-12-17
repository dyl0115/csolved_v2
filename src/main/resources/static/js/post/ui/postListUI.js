import * as navigationUI from '../../global/utils/navigationUI.js';
import * as paginationUI from '../../global/utils/paginationUI.js';
import * as timeUI from '../../global/utils/timeUI.js';
import * as sortUI from '../../global/utils/sortUI.js'

export function init()
{
    navigationUI.init();
    paginationUI.init('#post-pagination', onPageChange);
    timeUI.init();
    sortUI.init();
}

function onPageChange(requiredPage)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', requiredPage);
    window.location.href = url;
}

