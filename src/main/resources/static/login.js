const { createApp, ref, reactive } = Vue;

createApp({
    setup() {
        // 认证状态
        const authMode = ref('login'); // 'login' 或 'register'
        const authForm = reactive({
            username: '',
            password: ''
        });
        const loading = ref(false);
        const error = ref('');
        const success = ref('');
        
        // 检查用户是否已登录
        const checkAuth = () => {
            const token = localStorage.getItem('token');
            const userId = localStorage.getItem('userId');
            
            if (token && userId) {
                // 如果已登录，跳转到待办事项页面
                window.location.href = 'todo.html';
            }
        };
        
        // 登录方法
        const login = async () => {
            try {
                loading.value = true;
                error.value = '';
                success.value = '';
                
                const response = await axios.post('/api/user/login', {
                    username: authForm.username,
                    password: authForm.password
                });
                
                if (response.data.code === 200) {
                    localStorage.setItem('token', response.data.data.token);
                    localStorage.setItem('userId', response.data.data.userId);
                    success.value = '登录成功，正在跳转...';
                    
                    // 延迟跳转，让用户看到成功消息
                    setTimeout(() => {
                        window.location.href = 'todo.html';
                    }, 1000);
                } else {
                    error.value = response.data.message || '登录失败，请检查用户名和密码';
                }
            } catch (err) {
                console.error('登录错误:', err);
                error.value = err.response?.data?.message || '登录失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 注册方法
        const register = async () => {
            try {
                loading.value = true;
                error.value = '';
                success.value = '';
                
                const response = await axios.post('/api/user/register', {
                    username: authForm.username,
                    password: authForm.password
                });
                
                if (response.data.code === 200) {
                    success.value = '注册成功，请登录';
                    authMode.value = 'login';
                    // 清空表单
                    authForm.username = '';
                    authForm.password = '';
                } else {
                    error.value = response.data.message || '注册失败';
                }
            } catch (err) {
                console.error('注册错误:', err);
                error.value = err.response?.data?.message || '注册失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 页面加载时检查登录状态
        checkAuth();
        
        return {
            authMode,
            authForm,
            loading,
            error,
            success,
            login,
            register
        };
    }
}).mount('#app');