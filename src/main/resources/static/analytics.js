const { createApp, ref, computed, onMounted, nextTick } = Vue;

createApp({
    setup() {
        // 数据状态
        const dailyStats = ref({});
        const todos = ref([]);
        const loading = ref(false);
        const error = ref('');
        const chartInstance = ref(null);
        
        // 计算属性
        const totalTasks = computed(() => todos.value.length);
        const completedTasks = computed(() => todos.value.filter(todo => todo.status === 1).length);
        const completionRate = computed(() => {
            if (totalTasks.value === 0) return 0;
            return Math.round((completedTasks.value / totalTasks.value) * 100);
        });
        const totalDays = computed(() => Object.keys(dailyStats.value).length);
        const hasData = computed(() => Object.keys(dailyStats.value).length > 0);
        
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
        
        // 获取每日统计数据
        const fetchDailyStats = async () => {
            try {
                const userId = localStorage.getItem('userId');
                const response = await axios.get(`/api/task/daily-stats?userId=${userId}`);
                
                if (response.data.code === 200) {
                    dailyStats.value = response.data.data || {};
                } else {
                    error.value = response.data.message || '获取统计数据失败';
                }
            } catch (err) {
                console.error('获取统计数据错误:', err);
                error.value = err.response?.data?.message || '获取统计数据失败，请稍后再试';
            }
        };
        
        // 获取任务列表（用于计算总体统计）
        const fetchTodos = async () => {
            try {
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
            }
        };
        
        // 创建图表
        const createChart = async () => {
            await nextTick(); // 确保DOM已更新
            
            // 等待一小段时间确保DOM完全渲染
            setTimeout(() => {
                const canvas = document.getElementById('dailyChart');
                if (!canvas || Object.keys(dailyStats.value).length === 0) {
                    console.log('Canvas not found or no data available');
                    return;
                }
                
                // 销毁现有图表
                if (chartInstance.value) {
                    chartInstance.value.destroy();
                }
                
                const ctx = canvas.getContext('2d');
                
                // 准备图表数据
                const sortedDates = Object.keys(dailyStats.value).sort();
                const labels = sortedDates.map(date => {
                    const d = new Date(date);
                    return `${d.getMonth() + 1}/${d.getDate()}`;
                });
                const data = sortedDates.map(date => dailyStats.value[date]);
                
                console.log('Creating chart with data:', { labels, data });
                
                chartInstance.value = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '每日完成任务数',
                            data: data,
                            borderColor: 'rgb(102, 126, 234)',
                            backgroundColor: 'rgba(102, 126, 234, 0.1)',
                            borderWidth: 3,
                            fill: true,
                            tension: 0.4,
                            pointBackgroundColor: 'rgb(102, 126, 234)',
                            pointBorderColor: '#fff',
                            pointBorderWidth: 2,
                            pointRadius: 6,
                            pointHoverRadius: 8
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                display: true,
                                position: 'top',
                                labels: {
                                    font: {
                                        size: 14
                                    },
                                    color: '#2c3e50'
                                }
                            },
                            tooltip: {
                                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                                titleColor: '#fff',
                                bodyColor: '#fff',
                                borderColor: 'rgb(102, 126, 234)',
                                borderWidth: 1
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1,
                                    color: '#6c757d',
                                    font: {
                                        size: 12
                                    }
                                },
                                grid: {
                                    color: 'rgba(0, 0, 0, 0.1)'
                                }
                            },
                            x: {
                                ticks: {
                                    color: '#6c757d',
                                    font: {
                                        size: 12
                                    }
                                },
                                grid: {
                                    color: 'rgba(0, 0, 0, 0.1)'
                                }
                            }
                        },
                        elements: {
                            point: {
                                hoverBackgroundColor: 'rgb(102, 126, 234)'
                            }
                        }
                    }
                });
            }, 100);
        };
        
        // 刷新数据
        const refreshData = async () => {
            loading.value = true;
            error.value = '';
            
            try {
                await fetchDailyStats();
                await fetchTodos();
                // 确保数据加载完成后再创建图表
                await nextTick();
                if (hasData.value) {
                    await createChart();
                }
            } catch (err) {
                console.error('刷新数据错误:', err);
                error.value = '刷新数据失败，请稍后再试';
            } finally {
                loading.value = false;
            }
        };
        
        // 页面加载时的初始化
        onMounted(async () => {
            if (checkAuth()) {
                await refreshData();
            }
        });
        
        return {
            dailyStats,
            todos,
            loading,
            error,
            totalTasks,
            completedTasks,
            completionRate,
            totalDays,
            hasData,
            logout,
            refreshData
        };
    }
}).mount('#app');