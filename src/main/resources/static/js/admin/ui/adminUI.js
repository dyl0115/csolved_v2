import * as adminService from '../service/adminService.js';

export function init()
{
    document.querySelector('.report-list-btn')?.addEventListener('click', getReports)
}

async function getReports()
{
    adminService.getReports();
}