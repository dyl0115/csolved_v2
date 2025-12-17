import * as authService from '../../auth/service/authService.js';
import * as adminService from '../../admin/service/reportService.js';
import {handleError} from "../error/errorHandler.js";

export function init()
{
    createMobileMenu();

    document.querySelectorAll('.sign-out-btn')
        ?.forEach(btn => btn.addEventListener('click', signOut));
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

function createMobileMenu()
{
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuButton && mobileMenu)
    {
        mobileMenuButton.addEventListener('click', function ()
        {
            mobileMenu.classList.toggle('hidden');
        });
    }
}