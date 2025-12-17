import * as adminService from '../service/reportService.js';
import * as navigationUI from '../../global/utils/navigationUI.js'
import * as paginationUI from '../../global/utils/paginationUI.js'
import * as reportService from '../../admin/service/reportService.js'
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    navigationUI.init();

    paginationUI.init('#report-pagination', onPageChange);

    // 신고글 검색
    document.getElementById('report-search-btn')
        ?.addEventListener('click', getReportList);

    // 엔터키로 신고글 검색
    document.getElementById('search-form')
        .addEventListener('keypress', function (e)
        {
            if (e.key === 'Enter')
            {
                e.preventDefault();
                getReportList();
            }
        });

    // 신고글 검색 초기화
    document.getElementById('report-search-reset-btn')
        ?.addEventListener('click', resetSearch);

    // 신고 사유 보기
    document.querySelectorAll('.report-reason-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            getReportReason(event)));

    // 신고된 글 승낙 (게시글은 삭제됨)
    document.querySelectorAll('.report-accept-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            acceptReports(event)));

    // 신고 기각 (게시글은 보존됨)
    document.querySelectorAll('.report-dismiss-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            dismissReports(event)));

    // 신고 처리 취소 (게시글 복원)
    document.querySelectorAll('.report-undo-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            undoReportAction(event)));

    // 신고 처리 재수락 (게시글 삭제)
    document.querySelectorAll('.report-reprocess-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            reprocessReports(event)));

    // 신고된 글 다시 대기로 (게시글 보존)
    document.querySelectorAll('.report-pending-btn')
        ?.forEach(btn => btn.addEventListener('click', (event) =>
            pendingReport(event)));

}

async function acceptReports(event)
{
    const element = event.currentTarget;

    const form = {
        status: 'APPROVED',
        adminId: parseInt(element.dataset.adminId),
        targetType: element.dataset.targetType,
        targetId: parseInt(element.dataset.targetId)
    }

    try
    {
        await reportService.acceptReports(form);
        alert('신고된 게시글을 삭제했습니다.')
        window.location.reload();
    }
    catch (error)
    {
        handleError(error);
    }
}

async function dismissReports(event)
{
    const element = event.currentTarget;

    const form = {
        status: 'REJECTED',
        adminId: parseInt(element.dataset.adminId),
        targetType: element.dataset.targetType,
        targetId: parseInt(element.dataset.targetId)
    }

    try
    {
        await reportService.dismissReports(form);
        alert('신고된 게시글을 보존했습니다.');
        window.location.reload();
    }
    catch (error)
    {
        handleError(error);
    }
}

async function undoReportAction(event)
{
    const element = event.currentTarget;

    const form = {
        status: 'REJECTED',
        adminId: parseInt(element.dataset.adminId),
        targetType: element.dataset.targetType,
        targetId: parseInt(element.dataset.targetId)
    }

    try
    {
        await reportService.undoReportAction(form);
        alert('삭제된 게시글을 복원했습니다.');
        window.location.reload();
    }
    catch (error)
    {
        handleError(error);
    }
}

async function reprocessReports(event)
{
    const element = event.currentTarget;

    const form = {
        status: 'APPROVED',
        adminId: parseInt(element.dataset.adminId),
        targetType: element.dataset.targetType,
        targetId: parseInt(element.dataset.targetId)
    }

    try
    {
        await reportService.reprocessReports(form);
        alert('신고된 게시글을 삭제했습니다.')
        window.location.reload();
    }
    catch (error)
    {
        handleError(error);
    }
}


async function pendingReport(event)
{
    const element = event.currentTarget;

    const form = {
        status: 'PENDING',
        adminId: parseInt(element.dataset.adminId),
        targetType: element.dataset.targetType,
        targetId: parseInt(element.dataset.targetId)
    }

    try
    {
        await reportService.pendingReports(form);
        alert('해당 게시글을 다시 대기중으로 변경했습니다.')
        window.location.reload();
    }
    catch (error)
    {
        handleError(error);
    }
}

async function getReportList()
{
    const form = {
        keyword: document.getElementById('keyword').value.trim() || null,
        status: document.getElementById('status').value || null,
        targetType: document.getElementById('targetType').value || null,
        sortType: document.getElementById('sortType').value || null,
        startDateTime: document.getElementById('startDateTime').value || null,
        endDateTime: document.getElementById('endDateTime').value || null,
        page: 1,
        size: 20
    }

    try
    {
        await adminService.getReportList(form);
    }
    catch (error)
    {
        handleError(error);
    }
}

function resetSearch()
{
    window.location.href = `/admin/report?page=1&size=20`;
}

async function getReportReason(event)
{
    const report = event.currentTarget.closest('.report-container');
    const reportId = report.id;

    try
    {
        document.getElementById('detail-content').textContent
            = await reportService.getReportReason(reportId);

        document.getElementById('detail-modal')
            .classList.remove('hidden');

        lucide.createIcons();
    }
    catch (error)
    {
        alert('상세 정보를 불러오는데 실패했습니다.');
    }
}


function onPageChange(requiredPage)
{
    const url = new URL(window.location.href);
    url.searchParams.set('page', requiredPage);
    window.location.href = url;
}