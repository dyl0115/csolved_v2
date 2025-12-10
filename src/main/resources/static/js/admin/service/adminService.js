import * as adminClient from '../client/adminClient.js';

export async function getReports()
{
    const param = {
        page: 1,
        size: 20,
        sortType: 'CREATED_AT',
    }

    adminClient.getReports(param);
}