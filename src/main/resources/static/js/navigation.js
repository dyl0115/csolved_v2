async function signOut()
{
    if (confirm('로그아웃 하시겠습니까?'))
    {
        try
        {
            const response = await fetch('/api/auth/signOut', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'}
            });

            if (response.ok)
            {
                const result = await response.json();
                if (result.success)
                {
                    window.location.replace('/auth/signIn');
                } else
                {
                    alert("로그아웃에 실패했습니다.");
                }
            }
        }
        catch (error)
        {
            alert("에러가 발생했습니다. 잠시후 다시 시도해주세요.");
        }
    }
}

async function withdraw()
{
    if (confirm('정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.'))
    {
        try
        {
            const response = await fetch('/api/auth/withdraw', {
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'}
            });

            if (response.ok)
            {
                const result = await response.json();
                if (result.success)
                {
                    alert('회원탈퇴가 완료되었습니다.');
                } else
                {
                    alert('회원탈퇴에 실패했습니다. 잠시후 다시 시도해주세요.');
                }
            }
        }
        catch (error)
        {
            alert("에러가 발생했습니다. 잠시후 다시 시도해주세요.");
        }
    }
}

async function goToReportList()
{
    const param = {
        page: 1,
        size: 20,
        sortType: 'CREATED_AT'
    };
    const queryString = new URLSearchParams(param).toString();
    window.location.href = `/admin/report?${queryString}`;
}

function initNavigation()
{
    // 모바일 메뉴 토글
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