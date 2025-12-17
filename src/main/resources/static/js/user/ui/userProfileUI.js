import * as fileClient from '../../file/client/fileClient.js'
import * as userProfileService from '../service/userProfileService.js'
import * as authService from '../../auth/service/authService.js';
import * as navigationUI from '../../global/utils/navigationUI.js'
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    navigationUI.init();

    // 프로필 이미지 프리뷰 갱신
    document.getElementById('profile-image-input')
        .addEventListener('change', (event) => handleImageSelect(event));

    // 프로필 업데이트 버튼 클릭 시 업데이트
    document.getElementById('profile-update-btn')
        .addEventListener('click', (event) => updateProfile(event));

    // 비밀번호 보이기 토글
    document.querySelectorAll('.password-toggle-btn')
        .forEach(btn => btn.addEventListener('click', (event) => togglePassword(event)))

    // 비밀번호 강도 체크
    document.getElementById('new-password-input')
        .addEventListener('input', (event) => checkPasswordStrength(event));

    // 비밀번호, 비밀번호 확인이 동일한지 체크
    document.getElementById('new-password-confirm-input')
        .addEventListener('input', () => checkPasswordMatch());

    // 비밀번호 변경
    document.getElementById('password-submit-btn')
        .addEventListener('click', updatePassword);

    // 회원탈퇴 진행 질문 버튼
    document.getElementById('withdraw-btn')
        .addEventListener('click', showDeleteModal);

    //  회원탈퇴 최종 진행 버튼
    document.getElementById('withdraw-confirm-btn')
        .addEventListener('click', withdraw);

    // 회원탈퇴 취소
    document.getElementById('withdraw-cancel-btn')
        .addEventListener('click', hideDeleteModal);

    // 모달 외부 클릭 시 닫기
    document.getElementById('delete-modal').addEventListener('click', function (e)
    {
        if (e.target === this) hideDeleteModal();
    });

    // ESC 키로 모달 닫기
    document.addEventListener('keydown', function (e)
    {
        if (e.key === 'Escape') hideDeleteModal();
    });
}

// 비밀번호 업데이트
async function updatePassword()
{
    const successMessage = document.getElementById('password-success-message');
    const errorMessage = document.getElementById('password-error-message');

    const form = {
        currentPassword: document.getElementById('current-password').value,
        newPassword: document.getElementById('new-password-input').value,
        newPasswordConfirm: document.getElementById('new-password-confirm-input').value,
    }

    try
    {
        await authService.updatePassword(form);
        errorMessage.classList.add('hidden');
        successMessage.classList.remove('hidden');

        alert('비밀번호가 성공적으로 변경되었습니다.');
        window.location.href = '/users/profile';
    }
    catch (error)
    {
        successMessage.classList.add('hidden');
        errorMessage.classList.remove('hidden');
        errorMessage.querySelector('#password-error-text').textContent = error.message;
    }
}

// 프로필 업데이트
async function updateProfile(event)
{
    const successMessage = document.getElementById('profile-success-message');
    const errorMessage = document.getElementById('profile-error-message');

    const form = {
        userId: document.getElementById('user-id').value,
        nickname: document.getElementById('nickname').value,
        profileImage: document.getElementById('profile-image-url').value
    }

    try
    {
        await userProfileService.updateProfile(form);
        errorMessage.classList.add('hidden');
        successMessage.classList.remove('hidden');

        alert('프로필이 성공적으로 변경되었습니다.');
        window.location.href = '/users/profile';
    }
    catch (error)
    {
        successMessage.classList.add('hidden');
        errorMessage.classList.remove('hidden');
        errorMessage.querySelector('#profile-error-text').textContent = error.message;
    }
}

// 프로필 이미지 선택 핸들러
async function handleImageSelect(event)
{
    const input = event.currentTarget;

    if (!(input.files && input.files[0])) return;

    const formData = new FormData();
    formData.append('image', input.files[0]);

    try
    {
        const imageSrc = await fileClient.uploadImage(formData);
        document.getElementById('profile-image-preview').src = imageSrc;
        document.getElementById('profile-image-url').value = imageSrc;
    }
    catch (error)
    {
        handleError(error);
    }
}


// 비밀번호 보기/숨기기 토글
function togglePassword(event)
{
    const passwordContainer = event.currentTarget.closest('.password-input-container');
    const input = passwordContainer.querySelector('.password-input');
    const icon = passwordContainer.querySelector('.password-icon');

    if (input.type === 'password')
    {
        input.type = 'text';
        icon.setAttribute('data-lucide', 'eye-off');
    } else
    {
        input.type = 'password';
        icon.setAttribute('data-lucide', 'eye');
    }
    lucide.createIcons();
}

// 비밀번호 강도 체크
function checkPasswordStrength(event)
{
    const password = event.currentTarget.value;
    const strengthDiv = document.getElementById('password-strength');
    const strengthBar = document.getElementById('strength-bar');
    const strengthText = document.getElementById('strength-text');

    if (password.length === 0)
    {
        strengthDiv.classList.add('hidden');
        return;
    }

    strengthDiv.classList.remove('hidden');

    let strength = 0;
    const checks = {
        length: password.length >= 8,
        uppercase: /[A-Z]/.test(password),
        lowercase: /[a-z]/.test(password),
        number: /[0-9]/.test(password),
        special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
    };

    // 각 조건 체크 및 UI 업데이트
    Object.keys(checks).forEach(key =>
    {
        const element = document.getElementById(key + '-check');
        const icon = element.querySelector('.check-icon');

        if (checks[key])
        {
            strength++;
            element.classList.remove('text-gray-600');
            element.classList.add('text-green-600');
            icon.setAttribute('data-lucide', 'check-circle');
        } else
        {
            element.classList.remove('text-green-600');
            element.classList.add('text-gray-600');
            icon.setAttribute('data-lucide', 'circle');
        }
    });

    lucide.createIcons();

    // 강도 바 업데이트
    const percentage = (strength / 5) * 100;
    strengthBar.style.width = percentage + '%';

    if (strength <= 2)
    {
        strengthBar.className = 'h-full transition-all duration-300 bg-red-500';
        strengthText.textContent = '약함';
        strengthText.className = 'text-xs font-medium w-16 text-red-500';
    } else if (strength <= 3)
    {
        strengthBar.className = 'h-full transition-all duration-300 bg-yellow-500';
        strengthText.textContent = '보통';
        strengthText.className = 'text-xs font-medium w-16 text-yellow-500';
    } else if (strength <= 4)
    {
        strengthBar.className = 'h-full transition-all duration-300 bg-blue-500';
        strengthText.textContent = '강함';
        strengthText.className = 'text-xs font-medium w-16 text-blue-500';
    } else
    {
        strengthBar.className = 'h-full transition-all duration-300 bg-green-500';
        strengthText.textContent = '매우 강함';
        strengthText.className = 'text-xs font-medium w-16 text-green-500';
    }
}

// 비밀번호 일치 확인
function checkPasswordMatch()
{
    const newPassword = document.getElementById('new-password-input').value;
    const confirmPassword = document.getElementById('new-password-confirm-input').value;
    const errorElement = document.getElementById('password-match-error');
    const successElement = document.getElementById('password-match-success');

    if (confirmPassword.length === 0)
    {
        errorElement.classList.add('hidden');
        successElement.classList.add('hidden');
        return;
    }

    if (newPassword === confirmPassword)
    {
        errorElement.classList.add('hidden');
        successElement.classList.remove('hidden');
    } else
    {
        errorElement.classList.remove('hidden');
        successElement.classList.add('hidden');
    }
}

async function withdraw()
{
    const errorMessage = document.getElementById('withdraw-error-message');

    const form = {
        password: document.getElementById('delete-confirm-password').value,
    }

    try
    {
        await authService.withdraw(form);
        hideDeleteModal();
        alert('회원탈퇴가 완료 되었습니다. 이용해주셔서 감사합니다.');
        window.location.href = '/auth/signIn';
    }
    catch (error)
    {
        errorMessage.classList.remove('hidden');
        if (error.code === 'VALIDATION_EXCEPTION')
        {
            errorMessage.querySelector('#withdraw-error-text').textContent = error.errors['password'];
        } else
        {
            errorMessage.querySelector('#withdraw-error-text').textContent = error.message;
        }
    }
}

// 삭제 모달 표시
function showDeleteModal()
{
    document.getElementById('delete-modal').classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

// 삭제 모달 숨기기
function hideDeleteModal()
{
    document.getElementById('delete-modal').classList.add('hidden');
    document.getElementById('withdraw-error-message').classList.add('hidden');
    document.body.style.overflow = 'auto';
    document.getElementById('delete-confirm-password').value = '';
}


