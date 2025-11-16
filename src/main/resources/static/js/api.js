const API_BASE_URL = 'http://localhost:8080';

window.API_ENDPOINTS = {
    auth: {
        signUp: `${API_BASE_URL}/auth/signUp`,
        signIn: `${API_BASE_URL}/api/auth/signIn`,
        signOut: `${API_BASE_URL}/auth/signOut`,
        withdraw: `${API_BASE_URL}/auth/withdraw`,
    },

    post: {
        list: `${API_BASE_URL}/post/list`
    },

    users: {}
};