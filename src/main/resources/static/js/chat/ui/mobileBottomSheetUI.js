/**
 * 모바일 AI 채팅 바텀시트 UI
 * - 1280px 이하에서 활성화
 * - 드래그로 높이 조절 가능 (50% / 80% / 닫기)
 */

const SHEET_STATES = {
    CLOSED: 'closed',
    HALF: 'half',      // 50%
    FULL: 'full',      // 80%
};

const state = {
    isOpen: false,
    currentState: SHEET_STATES.CLOSED,
    startY: 0,
    startHeight: 0,
    isDragging: false,
};

let bottomSheet = null;
let overlay = null;
let handle = null;
let fab = null;

export function init() {
    // 모바일에서만 초기화
    if (!isMobile()) {
        // 데스크탑이어도 리사이즈 대비 이벤트는 등록
        window.addEventListener('resize', handleResize);
        return;
    }

    createBottomSheetElements();
    bindEvents();
    
    // 리사이즈 시 모바일 여부 재확인
    window.addEventListener('resize', handleResize);
}

function isMobile() {
    return window.innerWidth <= 1280;
}

function handleResize() {
    if (isMobile()) {
        if (!bottomSheet) {
            createBottomSheetElements();
            bindEvents();
        }
        if (fab) {
            fab.style.display = 'flex';
        }
    } else {
        if (state.isOpen) {
            close();
        }
        if (fab) {
            fab.style.display = 'none';
        }
    }
}

function createBottomSheetElements() {
    // FAB (Floating Action Button)
    fab = document.createElement('button');
    fab.id = 'ai-chat-fab';
    fab.className = 'fixed bottom-6 right-6 z-40 w-14 h-14 rounded-full bg-blue-600 text-white shadow-lg hover:bg-blue-700 flex items-center justify-center transition-all duration-300 xl:hidden';
    fab.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 8V4H8"/>
            <rect width="16" height="12" x="4" y="8" rx="2"/>
            <path d="M2 14h2"/>
            <path d="M20 14h2"/>
            <path d="M15 13v2"/>
            <path d="M9 13v2"/>
        </svg>
    `;
    fab.setAttribute('aria-label', 'AI 도우미 열기');
    document.body.appendChild(fab);

    // Overlay (배경 딤 처리)
    overlay = document.createElement('div');
    overlay.id = 'bottom-sheet-overlay';
    overlay.className = 'fixed inset-0 z-40 bg-black/50 backdrop-blur-sm opacity-0 pointer-events-none transition-opacity duration-300';
    document.body.appendChild(overlay);

    // Bottom Sheet Container
    bottomSheet = document.createElement('div');
    bottomSheet.id = 'ai-bottom-sheet';
    bottomSheet.className = 'fixed bottom-0 left-0 right-0 z-50 bg-white rounded-t-3xl shadow-2xl transform translate-y-full transition-transform duration-300 ease-out flex flex-col';
    bottomSheet.style.height = '50vh';
    bottomSheet.style.maxHeight = '85vh';
    
    bottomSheet.innerHTML = `
        <!-- 드래그 핸들 -->
        <div id="sheet-handle" class="flex justify-center py-3 cursor-grab active:cursor-grabbing touch-none">
            <div class="w-12 h-1.5 bg-gray-300 rounded-full"></div>
        </div>
        
        <!-- 헤더 -->
        <div class="flex items-center justify-between px-4 pb-3 border-b border-gray-200">
            <div class="flex items-center gap-2">
                <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-blue-600">
                        <path d="M12 8V4H8"/>
                        <rect width="16" height="12" x="4" y="8" rx="2"/>
                        <path d="M2 14h2"/>
                        <path d="M20 14h2"/>
                        <path d="M15 13v2"/>
                        <path d="M9 13v2"/>
                    </svg>
                </div>
                <span class="font-semibold text-gray-800">AI 게시글 도우미</span>
            </div>
            <button id="sheet-close-btn" class="p-2 hover:bg-gray-100 rounded-full transition-colors" aria-label="닫기">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-gray-500">
                    <path d="M18 6 6 18"/>
                    <path d="m6 6 12 12"/>
                </svg>
            </button>
        </div>
        
        <!-- 채팅 메시지 영역 -->
        <div id="mobile-chat-messages" class="flex-1 overflow-y-auto p-4 bg-gray-50">
            <!-- 초기 메시지 -->
            <div class="chat-message flex justify-start mb-4">
                <div class="chat-bubble max-w-[85%] break-words bg-white rounded-2xl rounded-tl-sm px-4 py-3 shadow-sm border border-gray-200">
                    <div class="flex items-center gap-2 mb-1">
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-blue-600">
                            <path d="m12 3-1.912 5.813a2 2 0 0 1-1.275 1.275L3 12l5.813 1.912a2 2 0 0 1 1.275 1.275L12 21l1.912-5.813a2 2 0 0 1 1.275-1.275L21 12l-5.813-1.912a2 2 0 0 1-1.275-1.275L12 3Z"/>
                            <path d="M5 3v4"/><path d="M19 17v4"/><path d="M3 5h4"/><path d="M17 19h4"/>
                        </svg>
                        <span class="text-xs font-medium text-blue-600">AI 도우미</span>
                    </div>
                    <p class="text-sm text-gray-700">안녕하세요! 게시글 작성을 도와드릴게요. 어떤 주제로 글을 쓰고 싶으신가요?</p>
                </div>
            </div>
        </div>
        
        <!-- 채팅 입력 영역 -->
        <div class="p-4 bg-white border-t border-gray-200 safe-area-bottom">
            <div class="flex gap-2">
                <input type="text"
                       id="mobile-chat-input"
                       placeholder="메시지를 입력하세요..."
                       class="flex-1 px-4 py-3 border border-gray-300 rounded-full focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors text-base">
                <button id="mobile-chat-submit-btn"
                        class="w-12 h-12 bg-blue-600 text-white rounded-full hover:bg-blue-700 transition-colors flex items-center justify-center flex-shrink-0">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="m22 2-7 20-4-9-9-4Z"/><path d="M22 2 11 13"/>
                    </svg>
                </button>
            </div>
        </div>
    `;
    
    document.body.appendChild(bottomSheet);
    
    handle = document.getElementById('sheet-handle');
}

function bindEvents() {
    // FAB 클릭
    fab.addEventListener('click', toggle);
    
    // 오버레이 클릭으로 닫기
    overlay.addEventListener('click', close);
    
    // 닫기 버튼
    document.getElementById('sheet-close-btn').addEventListener('click', close);
    
    // 드래그 이벤트
    handle.addEventListener('mousedown', startDrag);
    handle.addEventListener('touchstart', startDrag, { passive: false });
    
    document.addEventListener('mousemove', onDrag);
    document.addEventListener('touchmove', onDrag, { passive: false });
    
    document.addEventListener('mouseup', endDrag);
    document.addEventListener('touchend', endDrag);
    
    // 모바일 채팅 입력 이벤트
    document.getElementById('mobile-chat-submit-btn').addEventListener('click', sendMobileMessage);
    document.getElementById('mobile-chat-input').addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMobileMessage();
        }
    });

    // 데스크탑 채팅 영역과 동기화
    syncWithDesktopChat();
}

function syncWithDesktopChat() {
    const desktopMessages = document.getElementById('chat-messages');
    const mobileMessages = document.getElementById('mobile-chat-messages');
    
    if (!desktopMessages || !mobileMessages) return;
    
    // MutationObserver로 데스크탑 채팅 변경 감지
    const observer = new MutationObserver(() => {
        // 데스크탑 메시지를 모바일로 복사
        const desktopChildren = Array.from(desktopMessages.children);
        const mobileChildren = Array.from(mobileMessages.children);
        
        // 새로운 메시지만 추가
        if (desktopChildren.length > mobileChildren.length) {
            for (let i = mobileChildren.length; i < desktopChildren.length; i++) {
                const clone = desktopChildren[i].cloneNode(true);
                
                // 모바일용 id 설정 (postChatUI.js에서 업데이트할 수 있도록)
                const messageText = clone.querySelector('.message-text');
                const originalText = desktopChildren[i].querySelector('.message-text');
                if (messageText && originalText && originalText.id) {
                    messageText.id = originalText.id + '-mobile';
                }
                
                mobileMessages.appendChild(clone);
            }
            mobileMessages.scrollTop = mobileMessages.scrollHeight;
        }
    });
    
    observer.observe(desktopMessages, { 
        childList: true
    });
}

function sendMobileMessage() {
    const mobileInput = document.getElementById('mobile-chat-input');
    const desktopInput = document.getElementById('chat-input');
    const desktopSubmitBtn = document.getElementById('chat-submit-btn');
    
    const message = mobileInput.value.trim();
    if (!message) return;
    
    // 데스크탑 input에 값을 넣고 전송 버튼 클릭
    desktopInput.value = message;
    desktopSubmitBtn.click();
    
    // 모바일 input 초기화
    mobileInput.value = '';
}

function toggle() {
    if (state.isOpen) {
        close();
    } else {
        open();
    }
}

function open() {
    state.isOpen = true;
    state.currentState = SHEET_STATES.HALF;
    
    bottomSheet.style.height = '50vh';
    bottomSheet.classList.remove('translate-y-full');
    bottomSheet.classList.add('translate-y-0');
    
    overlay.classList.remove('opacity-0', 'pointer-events-none');
    overlay.classList.add('opacity-100', 'pointer-events-auto');
    
    // FAB 숨기기
    fab.classList.add('scale-0', 'opacity-0');
    
    // body 스크롤 방지
    document.body.style.overflow = 'hidden';
    
    // 입력창에 포커스
    setTimeout(() => {
        document.getElementById('mobile-chat-input').focus();
    }, 300);
}

function close() {
    state.isOpen = false;
    state.currentState = SHEET_STATES.CLOSED;
    
    bottomSheet.classList.remove('translate-y-0');
    bottomSheet.classList.add('translate-y-full');
    
    overlay.classList.remove('opacity-100', 'pointer-events-auto');
    overlay.classList.add('opacity-0', 'pointer-events-none');
    
    // FAB 다시 표시
    fab.classList.remove('scale-0', 'opacity-0');
    
    // body 스크롤 복원
    document.body.style.overflow = '';
}

function startDrag(e) {
    state.isDragging = true;
    state.startY = e.type === 'touchstart' ? e.touches[0].clientY : e.clientY;
    state.startHeight = bottomSheet.offsetHeight;
    
    bottomSheet.style.transition = 'none';
    handle.style.cursor = 'grabbing';
    
    e.preventDefault();
}

function onDrag(e) {
    if (!state.isDragging) return;
    
    const clientY = e.type === 'touchmove' ? e.touches[0].clientY : e.clientY;
    const deltaY = state.startY - clientY;
    const newHeight = state.startHeight + deltaY;
    
    const minHeight = window.innerHeight * 0.2;
    const maxHeight = window.innerHeight * 0.85;
    
    if (newHeight >= minHeight && newHeight <= maxHeight) {
        bottomSheet.style.height = `${newHeight}px`;
    }
    
    e.preventDefault();
}

function endDrag() {
    if (!state.isDragging) return;
    
    state.isDragging = false;
    bottomSheet.style.transition = '';
    handle.style.cursor = 'grab';
    
    const currentHeight = bottomSheet.offsetHeight;
    const viewportHeight = window.innerHeight;
    const heightPercent = (currentHeight / viewportHeight) * 100;
    
    // 스냅 로직
    if (heightPercent < 30) {
        // 30% 미만이면 닫기
        close();
    } else if (heightPercent < 65) {
        // 30-65%면 50%로 스냅
        bottomSheet.style.height = '50vh';
        state.currentState = SHEET_STATES.HALF;
    } else {
        // 65% 이상이면 80%로 스냅
        bottomSheet.style.height = '80vh';
        state.currentState = SHEET_STATES.FULL;
    }
}

// 외부에서 사용할 수 있는 함수들
export { open, close, toggle };
