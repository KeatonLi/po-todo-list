#!/bin/bash

# SQLite数据库初始化脚本
# 确保数据目录存在并设置正确权限

set -e

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}[INFO]${NC} 初始化SQLite数据库..."

# 创建数据目录
if [ ! -d "./data" ]; then
    mkdir -p ./data
    echo -e "${GREEN}[SUCCESS]${NC} 数据目录创建成功: ./data"
else
    echo -e "${GREEN}[INFO]${NC} 数据目录已存在: ./data"
fi

# 设置目录权限
chmod 755 ./data
echo -e "${GREEN}[SUCCESS]${NC} 数据目录权限设置完成"

# 检查数据库文件
if [ ! -f "./data/potodo.db" ]; then
    echo -e "${YELLOW}[INFO]${NC} 数据库文件不存在，将在应用首次启动时自动创建"
else
    echo -e "${GREEN}[INFO]${NC} 数据库文件已存在: ./data/potodo.db"
    # 显示数据库文件信息
    ls -la ./data/potodo.db
fi

echo -e "${GREEN}[SUCCESS]${NC} 数据库初始化完成！"
echo -e "${YELLOW}[提示]${NC} 数据将持久化保存在 ./data 目录中"
echo -e "${YELLOW}[提示]${NC} 容器重启或重新部署时，数据不会丢失"