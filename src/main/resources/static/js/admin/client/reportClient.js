export async function getReportList(params)
{
    window.location.href = `/admin/report?${params.toString()}`;
}

export async function getReportReason(reportId)
{
    const response = await fetch(`/api/report/${reportId}`);
    return response.text();
}

// 신고 수락 (게시글 삭제)
export async function acceptReports(request)
{
    const response = await fetch(`/api/report/approve`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}

// 신고 기각 (게시글 보존)
export async function dismissReports(request)
{
    const response = await fetch(`/api/report/reject`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}

// 신고 처리 취소 (삭제되었던 게시글 복원)
export async function undoReportAction(request)
{
    const response = await fetch(`/api/report/restore`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}

// 신고 기각 되었던 게시글 다시 수락 (게시글은 다시 삭제)
export async function reprocessReports(request)
{
    const response = await fetch(`/api/report/reprocess`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}

// 처리 완료 했던 게시글 다시 Pending으로
export async function pendingReports(request)
{
    const response = await fetch(`/api/report/re-pending`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    });

    if (!response.ok)
    {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
}


