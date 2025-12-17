import * as fileClient from '../../file/client/fileClient.js'
import * as userProfileService from '../service/userProfileService.js'
import {handleError} from "../../global/error/errorHandler.js";

export function init()
{
    document.getElementById('profile-image-input')
        .addEventListener('change', (event) => handleImageSelect(event));

    document.getElementById('profile-update-btn')
        .addEventListener('click', (event) => updateProfile(event));

    // 모달 외부 클릭 시 닫기
    document.getElementById('deleteModal').addEventListener('click', function (e)
    {
        if (e.target === this) hideDeleteModal();
    });

    // ESC 키로 모달 닫기
    document.addEventListener('keydown', function (e)
    {
        if (e.key === 'Escape') hideDeleteModal();
    });
}

// 프로필 업데이트
async function updateProfile(event)
{
    const profile = event.currentTarget.closest('.profile-container');
    const userId = profile.querySelector('#user-id').value;
    const nickname = profile.querySelector('#nickname').value;
    const profileImage = profile.querySelector('#profile-image-url').value;
    const successMessage = document.getElementById('profile-success-message');
    const errorMessage = document.getElementById('profile-error-message');

    const form = {
        userId: userId,
        nickname: nickname,
        profileImage: profileImage
    }

    try
    {
        await userProfileService.updateProfile(form);
        errorMessage.classList.add('hidden');
        successMessage.classList.remove('hidden');
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
function togglePassword(inputId)
{
    const input = document.getElementById(inputId);
    const icon = document.getElementById(inputId + 'Icon');

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
function checkPasswordStrength(password)
{
    const strengthDiv = document.getElementById('passwordStrength');
    const strengthBar = document.getElementById('strengthBar');
    const strengthText = document.getElementById('strengthText');

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
        const element = document.getElementById(key + 'Check');
        const icon = element.querySelector('i');

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
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorElement = document.getElementById('passwordMatchError');
    const successElement = document.getElementById('passwordMatchSuccess');

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

// 삭제 모달 표시
function showDeleteModal()
{
    document.getElementById('deleteModal').classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

// 삭제 모달 숨기기
function hideDeleteModal()
{
    document.getElementById('deleteModal').classList.add('hidden');
    document.body.style.overflow = 'auto';
    document.getElementById('deleteConfirmPassword').value = '';
}


