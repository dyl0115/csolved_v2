import * as authClient from "../client/authClient.js";

export async function signOut()
{
    await authClient.signOut();
}

export async function withdraw()
{
    await authClient.withdraw();
}
