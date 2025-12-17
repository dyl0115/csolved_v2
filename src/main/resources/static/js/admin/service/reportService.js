import * as reportClient from '../client/reportClient.js';

export async function getReportList(formData)
{
    Object.keys(formData)
        .forEach(key =>
        {
            if (formData[key] === null || formData === '') delete formData[key];
        })

    const params = new URLSearchParams();

    Object.keys(formData)
        .forEach(key =>
        {
            if (formData[key] !== undefined) params.append(key, formData[key]);
        })

    return reportClient.getReportList(params);
}

export async function getReportReason(reportId)
{
    return reportClient.getReportReason(reportId);
}


export async function acceptReports(form)
{
    const request = {
        status: form.status,
        adminId: form.adminId,
        targetType: form.targetType,
        targetId: form.targetId
    }

    return reportClient.acceptReports(request);
}

export async function dismissReports(form)
{
    const request = {
        status: form.status,
        adminId: form.adminId,
        targetType: form.targetType,
        targetId: form.targetId
    }

    return reportClient.dismissReports(request);
}

export async function undoReportAction(form)
{
    const request = {
        status: form.status,
        adminId: form.adminId,
        targetType: form.targetType,
        targetId: form.targetId
    }

    return reportClient.undoReportAction(request);
}

export async function reprocessReports(form)
{
    const request = {
        status: form.status,
        adminId: form.adminId,
        targetType: form.targetType,
        targetId: form.targetId
    }

    return reportClient.reprocessReports(request);
}

export async function pendingReports(form)
{
    const request = {
        status: form.status,
        adminId: form.adminId,
        targetType: form.targetType,
        targetId: form.targetId
    }

    return reportClient.pendingReports(request);
}