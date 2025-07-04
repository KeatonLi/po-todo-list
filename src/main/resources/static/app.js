// 创建Vue应用
const { createApp, ref, reactive, onMounted, computed } = Vue;

const app = createApp({
    setup() {
        // 认证状态
        const isLoggedIn = ref(false);
        const authMode = ref('login'); // 'login' 或 'register'
        const authForm = reactive({
            username: '',
            password: ''
        });
        const loading = ref(false);
        const error = ref('');
        const success = ref('');
        
        // Todo状态
        const todos = ref([]);
        const newTodo = reactive({
            title: '',
            describe: ''
        });
        
        // 图表状态
        const showChart = ref(false);
        const chartInstance = ref(null);
        const dailyStats = ref({});
        
        // 检查用户是否已登录
        const checkAuth = async () => {
            const token = localStorage.getItem('token');
            const userId = localStorage.getItem('userId');
            
            if (token && userId) {
                isLoggedIn.value = true;
                await fetchTodos();
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
                    isLoggedIn.value = true;
                    await fetchTodos();
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
        
        // 登出方法
        const logout = () => {
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            isLoggedIn.value = false;
            todos.value = [];
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
                    completedAt: Date.now()
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
        const deleteTodo = async (id) => {
            try {
                loading.value = true;
                error.value = '';
                
                const response = await axios.post('/api/task/delete', { id });
                
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
        
        // 获取每日完成任务统计
        const fetchDailyStats = async () => {
            try {
                const userId = localStorage.getItem('userId');
                const response = await axios.get(`/api/task/daily-stats?userId=${userId}`);
                
                if (response.data.code === 200) {
                    dailyStats.value = response.data.data || {};
                    updateChart();
                } else {
                    error.value = response.data.message || '获取统计数据失败';
                }
            } catch (err) {
                console.error('获取统计数据错误:', err);
                error.value = err.response?.data?.message || '获取统计数据失败，请稍后再试';
            }
        };
        
        // 初始化图表
        const initChart = () => {
            const canvas = document.getElementById('dailyChart');
            if (!canvas) return;
            
            const ctx = canvas.getContext('2d');
            
            // 销毁现有图表实例
            if (chartInstance.value) {
                chartInstance.value.destroy();
            }
            
            const dates = Object.keys(dailyStats.value);
            const counts = Object.values(dailyStats.value);
            
            chartInstance.value = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: dates.map(date => {
                        const d = new Date(date);
                        return `${d.getMonth() + 1}/${d.getDate()}`;
                    }),
                    datasets: [{
                        label: '完成任务数',
                        data: counts,
                        borderColor: '#3498db',
                        backgroundColor: 'rgba(52, 152, 219, 0.1)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4,
                        pointBackgroundColor: '#3498db',
                        pointBorderColor: '#2980b9',
                        pointRadius: 4,
                        pointHoverRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    interaction: {
                        intersect: false,
                        mode: 'index'
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        },
                        tooltip: {
                            backgroundColor: 'rgba(0, 0, 0, 0.8)',
                            titleColor: '#fff',
                            bodyColor: '#fff',
                            borderColor: '#3498db',
                            borderWidth: 1
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 1,
                                color: '#666'
                            },
                            title: {
                                display: true,
                                text: '完成任务数量',
                                color: '#333'
                            },
                            grid: {
                                color: 'rgba(0, 0, 0, 0.1)'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: '日期',
                                color: '#333'
                            },
                            ticks: {
                                color: '#666'
                            },
                            grid: {
                                color: 'rgba(0, 0, 0, 0.1)'
                            }
                        }
                    },
                    elements: {
                        point: {
                            hoverBorderWidth: 3
                        }
                    }
                }
            });
        };
        
        // 更新图表
        const updateChart = () => {
            if (showChart.value) {
                // 使用 nextTick 确保 DOM 已更新
                Vue.nextTick(() => {
                    initChart();
                });
            }
        };
        
        // 监听图表显示状态变化
        Vue.watch(showChart, (newValue) => {
            if (newValue) {
                fetchDailyStats();
            }
        });
        
        // 设置axios默认配置
        axios.defaults.baseURL = '';
        axios.interceptors.request.use(config => {
            const token = localStorage.getItem('token');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        });
        
        // 页面加载时检查认证状态
        onMounted(() => {
            checkAuth();
        });
        
        return {
            // 认证相关
            isLoggedIn,
            authMode,
            authForm,
            loading,
            error,
            success,
            login,
            register,
            logout,
            
            // Todo相关
            todos,
            newTodo,
            addTodo,
            toggleComplete,
            toggleStar,
            deleteTodo,
            
            // 图表相关
            showChart,
            dailyStats,
            fetchDailyStats,
            initChart,
            updateChart
        };
    }
});

app.mount('#app');