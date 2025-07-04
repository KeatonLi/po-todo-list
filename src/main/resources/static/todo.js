const { createApp, ref, reactive, computed, onMounted } = Vue;

createApp({
    setup() {
        // Todo状态
        const todos = ref([]);
        const newTodo = reactive({
            title: '',
            describe: ''
        });
        const loading = ref(false);
        const error = ref('');
        
        // 计算属性
        const totalTodos = computed(() => todos.value.length);
        const completedTodos = computed(() => todos.value.filter(todo => todo.status === 1).length);
        const pendingTodos = computed(() => todos.value.filter(todo => todo.status === 0).length);
        const starredTodos = computed(() => todos.value.filter(todo => todo.star === 1).length);
        
        // 检查用户是否已登录
        const checkAuth = () => {
            const token = localStorage.getItem('token');
            const userId = localStorage.getItem('userId');
            
            if (!token || !userId) {
                // 如果未登录，跳转到登录页面
                window.location.href = 'login.html';
                return false;
            }
            return true;
        };
        
        // 登出方法
        const logout = () => {
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            window.location.href = 'login.html';
        };
        
        // 获取Todo列表
        const fetchTodos = async () => {
            try {
                loading.value = true;
                error.value = '';
                
                const userId = localStorage.getItem('userId');
                const response = await axios.get(`/api/task/list?userId=${userId}`);
                
                if (response.data.code === 200) {
                    todos.value = response.data.data || [];
                } else {
                    error.value = response.data.message || '获取任务列表失败';
                }
            } catch (err) {
                console.error('获取任务列表错误:', err);
                error.value = err.response?.data?.message || '获取任务列表失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 添加新Todo
        const addTodo = async () => {
            if (!newTodo.title.trim()) return;
            
            try {
                loading.value = true;
                error.value = '';
                
                const userId = localStorage.getItem('userId');
                const response = await axios.post('/api/task/add', {
                    userId: userId,
                    title: newTodo.title,
                    describe: newTodo.describe || ''
                });
                
                if (response.data.code === 200) {
                    await fetchTodos();
                    newTodo.title = '';
                    newTodo.describe = '';
                } else {
                    error.value = response.data.message || '添加任务失败';
                }
            } catch (err) {
                console.error('添加任务错误:', err);
                error.value = err.response?.data?.message || '添加任务失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 切换Todo完成状态
        const toggleComplete = async (todo) => {
            try {
                loading.value = true;
                error.value = '';
                
                const newStatus = todo.status === 1 ? 0 : 1;
                const response = await axios.post('/api/task/update', {
                    id: todo.id,
                    status: newStatus,
                    completedAt: newStatus === 1 ? Date.now() : null
                });
                
                if (response.data.code === 200) {
                    await fetchTodos();
                } else {
                    error.value = response.data.message || '更新任务状态失败';
                }
            } catch (err) {
                console.error('更新任务状态错误:', err);
                error.value = err.response?.data?.message || '更新任务状态失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 切换Todo星标状态
        const toggleStar = async (todo) => {
            try {
                loading.value = true;
                error.value = '';
                
                const newStar = todo.star === 1 ? 0 : 1;
                const response = await axios.post('/api/task/update', {
                    id: todo.id,
                    star: newStar
                });
                
                if (response.data.code === 200) {
                    await fetchTodos();
                } else {
                    error.value = response.data.message || '更新任务星标失败';
                }
            } catch (err) {
                console.error('更新任务星标错误:', err);
                error.value = err.response?.data?.message || '更新任务星标失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 删除Todo
        const deleteTodo = async (todoId) => {
            if (!confirm('确定要删除这个任务吗？')) {
                return;
            }
            
            try {
                loading.value = true;
                error.value = '';
                
                const response = await axios.post('/api/task/delete', {
                    id: todoId
                });
                
                if (response.data.code === 200) {
                    await fetchTodos();
                } else {
                    error.value = response.data.message || '删除任务失败';
                }
            } catch (err) {
                console.error('删除任务错误:', err);
                error.value = err.response?.data?.message || '删除任务失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 页面加载时的初始化
        onMounted(async () => {
            if (checkAuth()) {
                await fetchTodos();
            }
        });
        
        return {
            todos,
            newTodo,
            loading,
            error,
            totalTodos,
            completedTodos,
            pendingTodos,
            starredTodos,
            logout,
            addTodo,
            toggleComplete,
            toggleStar,
            deleteTodo
        };
    }
}).mount('#app');