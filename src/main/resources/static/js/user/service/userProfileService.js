import * as userProfileClient from '../client/userProfileClient.js'

export async function updateProfile(form)
{
    const request = {
        userId: form.userId,
        nickname: form.nickname,
        profileImage: form.profileImage,
    }

    return userProfileClient.updateProfile(request);
}