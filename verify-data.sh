#!/bin/bash

# 数据持久化验证脚本
# 用于验证SQLite数据库是否正确挂载和持久化

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Po-Todo-List 数据持久化验证 ===${NC}"
echo

# 检查本地数据目录
echo -e "${YELLOW}[检查]${NC} 本地数据目录..."
if [ -d "./data" ]; then
    echo -e "${GREEN}✓${NC} 本地数据目录存在: ./data"
    ls -la ./data/
else
    echo -e "${RED}✗${NC} 本地数据目录不存在"
    exit 1
fi
echo

# 检查数据库文件
echo -e "${YELLOW}[检查]${NC} SQLite数据库文件..."
if [ -f "./data/potodo.db" ]; then
    echo -e "${GREEN}✓${NC} 数据库文件存在: ./data/potodo.db"
    echo -e "${BLUE}[信息]${NC} 文件大小: $(du -h ./data/potodo.db | cut -f1)"
    echo -e "${BLUE}[信息]${NC} 修改时间: $(stat -f "%Sm" ./data/potodo.db)"
else
    echo -e "${YELLOW}!${NC} 数据库文件不存在，将在应用首次启动时创建"
fi
echo

# 检查容器状态
echo -e "${YELLOW}[检查]${NC} Docker容器状态..."
if docker-compose ps | grep -q "po-todo-list-app"; then
    if docker-compose ps | grep -q "Up"; then
        echo -e "${GREEN}✓${NC} 容器正在运行"
        
        # 检查容器内数据目录
        echo -e "${YELLOW}[检查]${NC} 容器内数据目录挂载..."
        docker-compose exec po-todo-list ls -la /app/data/ || echo -e "${RED}✗${NC} 无法访问容器内数据目录"
        
        # 检查数据库连接
        echo -e "${YELLOW}[检查]${NC} 应用健康状态..."
        if curl -s http://localhost:8081/ > /dev/null; then
            echo -e "${GREEN}✓${NC} 应用响应正常"
        else
            echo -e "${RED}✗${NC} 应用无响应"
        fi
    else
        echo -e "${RED}✗${NC} 容器未运行"
    fi
else
    echo -e "${YELLOW}!${NC} 容器未创建，请先运行部署脚本"
fi
echo

# 检查docker-compose配置
echo -e "${YELLOW}[检查]${NC} Docker Compose卷挂载配置..."
if grep -q "./data:/app/data" docker-compose.yml; then
    echo -e "${GREEN}✓${NC} 数据卷挂载配置正确"
else
    echo -e "${RED}✗${NC} 数据卷挂载配置错误"
fi

if grep -q "SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/potodo.db" docker-compose.yml; then
    echo -e "${GREEN}✓${NC} 数据库路径环境变量配置正确"
else
    echo -e "${RED}✗${NC} 数据库路径环境变量配置错误"
fi
echo

echo -e "${BLUE}=== 验证完成 ===${NC}"
echo -e "${GREEN}[提示]${NC} 数据将持久化保存在 ./data 目录中"
echo -e "${GREEN}[提示]${NC} 容器重启或重新部署时，数据不会丢失"
echo -e "${YELLOW}[注意]${NC} 如需备份数据，请复制整个 ./data 目录"