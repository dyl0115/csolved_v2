import * as authService from '../auth/service/authService.js';
import * as adminService from '../admin/service/adminService.js';
import {handleError} from "../common/error/errorHandler.js";

export function init()
{
    createMobileMenu();
    document.querySelector('.sign-out-btn')?.addEventListener('click', signOut);
    document.querySelector('.withdraw-btn')?.addEventListener('click', withdraw);
    document.querySelector('.report-list-btn')?.addEventListener('click', adminService.getReports);
}

async function signOut()
{
    if (!confirm('로그아웃 하시겠습니까?')) return;

    try
    {
        await authService.signOut();
        window.location.replace('/auth/signIn');
    }
    catch (error)
    {
        handleError(error);
    }
}

async function withdraw()
{
    if (!confirm('정말 탈퇴하시겠습니까?')) return;

    try
    {
        await authService.withdraw();
        alert('회원탈퇴가 완료되었습니다.');
        window.location.replace('/auth/signIn');
    }
    catch (error)
    {
        handleError(error);
    }
}

function createMobileMenu()
{
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuButton && mobileMenu)
    {
        mobileMenuButton.addEventListener('click', function ()
        {
            if (mobileMenu.style.display === 'none' || mobileMenu.style.display === '')
            {
                mobileMenu.style.display = 'block';
            } else
            {
                mobileMenu.style.display = 'none';
            }
        });
    }
}