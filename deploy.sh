#!/bin/bash

# Po-Todo-List 快速部署脚本
# 使用方法: ./deploy.sh [选项]
# 选项:
#   build    - 构建Docker镜像
#   start    - 启动服务
#   stop     - 停止服务
#   restart  - 重启服务
#   logs     - 查看日志
#   clean    - 清理容器和镜像

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目配置
PROJECT_NAME="po-todo-list"
CONTAINER_NAME="po-todo-list-app"
IMAGE_NAME="po-todo-list:latest"
PORT="8081"

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

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
}

# 创建必要的目录
create_directories() {
    log_info "创建必要的目录..."
    mkdir -p data logs
    
    # 运行数据库初始化脚本
    if [ -f "./init-db.sh" ]; then
        log_info "运行数据库初始化脚本..."
        ./init-db.sh
    fi
    
    log_success "目录创建完成"
}

# 构建镜像
build_image() {
    log_info "开始构建 Docker 镜像..."
    docker-compose build --no-cache
    log_success "Docker 镜像构建完成"
}

# 启动服务
start_service() {
    log_info "启动 Po-Todo-List 服务..."
    docker-compose up -d
    
    # 等待服务启动
    log_info "等待服务启动..."
    sleep 10
    
    # 检查服务状态
    if docker-compose ps | grep -q "Up"; then
        log_success "服务启动成功！"
        log_info "访问地址: http://localhost:${PORT}"
        log_info "管理界面: http://localhost:${PORT}/login.html"
    else
        log_error "服务启动失败，请检查日志"
        docker-compose logs
    fi
}

# 停止服务
stop_service() {
    log_info "停止 Po-Todo-List 服务..."
    docker-compose down
    log_success "服务已停止"
}

# 重启服务
restart_service() {
    log_info "重启 Po-Todo-List 服务..."
    stop_service
    start_service
}

# 查看日志
view_logs() {
    log_info "查看服务日志..."
    docker-compose logs -f
}

# 清理容器和镜像
clean_all() {
    log_warning "这将删除所有相关的容器和镜像，数据将保留"
    read -p "确认继续？(y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        log_info "停止并删除容器..."
        docker-compose down --rmi all --volumes --remove-orphans
        log_success "清理完成"
    else
        log_info "操作已取消"
    fi
}

# 显示帮助信息
show_help() {
    echo "Po-Todo-List 部署脚本"
    echo ""
    echo "使用方法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  build    - 构建Docker镜像"
    echo "  start    - 启动服务"
    echo "  stop     - 停止服务"
    echo "  restart  - 重启服务"
    echo "  logs     - 查看日志"
    echo "  clean    - 清理容器和镜像"
    echo "  help     - 显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 build    # 构建镜像"
    echo "  $0 start    # 启动服务"
    echo "  $0 logs     # 查看日志"
}

# 主函数
main() {
    check_docker
    create_directories
    
    case "${1:-help}" in
        build)
            build_image
            ;;
        start)
            start_service
            ;;
        stop)
            stop_service
            ;;
        restart)
            restart_service
            ;;
        logs)
            view_logs
            ;;
        clean)
            clean_all
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"