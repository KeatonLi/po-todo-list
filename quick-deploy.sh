#!/bin/bash

# Po-Todo-List 一键部署脚本
# 适用于全新的服务器环境

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 配置变量
PROJECT_NAME="po-todo-list"
DEPLOY_DIR="/opt/po-todo-list"
GIT_REPO="https://github.com/your-username/po-todo-list.git"  # 替换为实际的仓库地址
SERVER_PORT="8081"

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查是否为 root 用户
check_root() {
    if [[ $EUID -eq 0 ]]; then
        log_warning "检测到 root 用户，建议使用普通用户运行此脚本"
        read -p "是否继续？(y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
}

# 检测操作系统
detect_os() {
    if [[ -f /etc/os-release ]]; then
        . /etc/os-release
        OS=$NAME
        VER=$VERSION_ID
    else
        log_error "无法检测操作系统"
        exit 1
    fi
    log_info "检测到操作系统: $OS $VER"
}

# 安装 Docker
install_docker() {
    if command -v docker &> /dev/null; then
        log_info "Docker 已安装，跳过安装步骤"
        return
    fi
    
    log_info "开始安装 Docker..."
    
    if [[ "$OS" == *"Ubuntu"* ]] || [[ "$OS" == *"Debian"* ]]; then
        # Ubuntu/Debian
        sudo apt update
        sudo apt install -y apt-transport-https ca-certificates curl gnupg lsb-release
        curl -fsSL https://get.docker.com -o get-docker.sh
        sudo sh get-docker.sh
        sudo usermod -aG docker $USER
    elif [[ "$OS" == *"CentOS"* ]] || [[ "$OS" == *"Red Hat"* ]]; then
        # CentOS/RHEL
        sudo yum install -y yum-utils
        sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
        sudo yum install -y docker-ce docker-ce-cli containerd.io
        sudo systemctl start docker
        sudo systemctl enable docker
        sudo usermod -aG docker $USER
    else
        log_error "不支持的操作系统: $OS"
        exit 1
    fi
    
    log_success "Docker 安装完成"
}

# 安装 Docker Compose
install_docker_compose() {
    if command -v docker-compose &> /dev/null; then
        log_info "Docker Compose 已安装，跳过安装步骤"
        return
    fi
    
    log_info "开始安装 Docker Compose..."
    
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    
    # 创建软链接（兼容性）
    sudo ln -sf /usr/local/bin/docker-compose /usr/bin/docker-compose
    
    log_success "Docker Compose 安装完成"
}

# 配置防火墙
setup_firewall() {
    log_info "配置防火墙..."
    
    if command -v ufw &> /dev/null; then
        # Ubuntu/Debian
        sudo ufw allow $SERVER_PORT/tcp
        sudo ufw allow 80/tcp
        sudo ufw allow 443/tcp
        log_info "UFW 防火墙规则已添加"
    elif command -v firewall-cmd &> /dev/null; then
        # CentOS/RHEL
        sudo firewall-cmd --permanent --add-port=$SERVER_PORT/tcp
        sudo firewall-cmd --permanent --add-port=80/tcp
        sudo firewall-cmd --permanent --add-port=443/tcp
        sudo firewall-cmd --reload
        log_info "Firewalld 防火墙规则已添加"
    else
        log_warning "未检测到防火墙，请手动配置端口 $SERVER_PORT, 80, 443"
    fi
}

# 下载项目代码
download_project() {
    log_info "下载项目代码..."
    
    # 创建部署目录
    sudo mkdir -p $DEPLOY_DIR
    sudo chown $USER:$USER $DEPLOY_DIR
    
    if [[ -d "$DEPLOY_DIR/.git" ]]; then
        log_info "项目已存在，更新代码..."
        cd $DEPLOY_DIR
        git pull
    else
        if [[ "$GIT_REPO" == *"your-username"* ]]; then
            log_warning "请先配置正确的 Git 仓库地址"
            log_info "当前目录的文件将被复制到部署目录"
            cp -r . $DEPLOY_DIR/
        else
            git clone $GIT_REPO $DEPLOY_DIR
        fi
    fi
    
    cd $DEPLOY_DIR
    log_success "项目代码下载完成"
}

# 部署应用
deploy_application() {
    log_info "开始部署应用..."
    
    # 确保在项目目录
    cd $DEPLOY_DIR
    
    # 创建必要的目录
    mkdir -p data logs backup
    
    # 设置权限
    chmod +x deploy.sh
    
    # 构建和启动
    ./deploy.sh build
    ./deploy.sh start
    
    log_success "应用部署完成"
}

# 验证部署
verify_deployment() {
    log_info "验证部署状态..."
    
    # 等待服务启动
    sleep 15
    
    # 检查容器状态
    if docker-compose ps | grep -q "Up"; then
        log_success "容器运行正常"
    else
        log_error "容器启动失败"
        docker-compose logs
        return 1
    fi
    
    # 检查端口
    if netstat -tlnp | grep -q ":$SERVER_PORT"; then
        log_success "端口 $SERVER_PORT 监听正常"
    else
        log_warning "端口 $SERVER_PORT 未监听，请检查配置"
    fi
    
    # 尝试访问应用
    if command -v curl &> /dev/null; then
        if curl -s "http://localhost:$SERVER_PORT" > /dev/null; then
            log_success "应用响应正常"
        else
            log_warning "应用可能还在启动中，请稍后检查"
        fi
    fi
}

# 显示部署信息
show_deployment_info() {
    echo ""
    echo "======================================"
    echo "🎉 Po-Todo-List 部署完成！"
    echo "======================================"
    echo ""
    echo "📍 部署目录: $DEPLOY_DIR"
    echo "🌐 访问地址: http://$(hostname -I | awk '{print $1}'):$SERVER_PORT"
    echo "🔐 登录页面: http://$(hostname -I | awk '{print $1}'):$SERVER_PORT/login.html"
    echo "📊 数据分析: http://$(hostname -I | awk '{print $1}'):$SERVER_PORT/analytics.html"
    echo ""
    echo "🔧 管理命令:"
    echo "  查看日志: cd $DEPLOY_DIR && ./deploy.sh logs"
    echo "  重启服务: cd $DEPLOY_DIR && ./deploy.sh restart"
    echo "  停止服务: cd $DEPLOY_DIR && ./deploy.sh stop"
    echo ""
    echo "📚 详细文档: $DEPLOY_DIR/DEPLOYMENT.md"
    echo "======================================"
}

# 主函数
main() {
    echo "🚀 Po-Todo-List 一键部署脚本"
    echo "======================================"
    
    check_root
    detect_os
    install_docker
    install_docker_compose
    setup_firewall
    download_project
    deploy_application
    verify_deployment
    show_deployment_info
    
    log_success "部署完成！请根据上述信息访问您的应用。"
}

# 错误处理
trap 'log_error "部署过程中发生错误，请检查日志"' ERR

# 执行主函数
main "$@"