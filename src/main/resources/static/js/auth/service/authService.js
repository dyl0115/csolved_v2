import * as authClient from "../client/authClient.js";

export async function signOut()
{
    await authClient.signOut();
}

export async function withdraw(form)
{
    const request = {
        password: form.password,
    }

    await authClient.withdraw(request);
}

export async function updatePassword(form)
{
    const request = {
        currentPassword: form.currentPassword,
        newPassword: form.newPassword,
        newPasswordConfirm: form.newPasswordConfirm
    }

    return authClient.updatePassword(request);
}
