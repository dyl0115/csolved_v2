export async function getReports(param)
{
    const queryString = new URLSearchParams(param).toString();
    window.location.href = `/admin/report?${queryString}`;
}
