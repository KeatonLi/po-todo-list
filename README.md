# Po-Todo-List 📝

一个基于 Spring Boot + Vue.js + SQLite 的现代化待办事项管理系统

## 📖 项目介绍

Po-Todo-List 是一个功能完整的待办任务清单应用，旨在帮助用户高效管理日常任务，提升工作效率。系统提供了直观的任务管理界面、数据统计分析功能，以及用户认证系统。

## ✨ 主要功能

### 🔐 用户管理
- 用户注册与登录
- JWT Token 身份认证
- 安全的密码管理

### 📋 任务管理
- ✅ 创建、编辑、删除待办任务
- ⭐ 重要任务星标标记
- 📝 任务描述和详细信息
- 🔄 任务状态切换（待完成/已完成）
- 📅 任务创建和完成时间记录

### 📊 数据分析
- 📈 每日任务完成统计图表
- 📋 任务总览和完成率统计
- 🎯 可视化数据展示（Chart.js）
- 📆 最近30天完成趋势分析

### 🎨 用户界面
- 📱 响应式设计，支持移动端
- 🎯 现代化UI设计
- 🔄 单页应用体验
- 🌈 美观的渐变色彩方案

## 🛠️ 技术栈

### 后端技术
- **Spring Boot 2.5.5** - 主框架
- **MyBatis Plus** - ORM框架
- **SQLite** - 轻量级数据库
- **JWT** - 身份认证
- **Druid** - 数据库连接池
- **Lombok** - 代码简化

### 前端技术
- **Vue.js 3** - 前端框架（组合式API）
- **Axios** - HTTP客户端
- **Chart.js** - 图表库
- **CSS3** - 样式设计
- **HTML5** - 页面结构

### 开发工具
- **Maven** - 项目管理
- **Java 8** - 开发语言
- **Git** - 版本控制

## 📁 项目结构

```
po-todo-list/
├── src/main/java/cn/todolist/po/
│   ├── config/          # 配置类
│   ├── controller/      # 控制器层
│   ├── mapper/          # 数据访问层
│   ├── model/           # 实体类
│   ├── service/         # 业务逻辑层
│   └── utils/           # 工具类
├── src/main/resources/
│   ├── static/          # 静态资源
│   │   ├── index.html   # 欢迎页面
│   │   ├── login.html   # 登录注册页面
│   │   ├── todo.html    # 任务管理页面
│   │   ├── analytics.html # 数据分析页面
│   │   └── *.js         # 对应的JavaScript文件
│   ├── DDL/             # 数据库初始化脚本
│   └── application.yml  # 应用配置
└── data/                # SQLite数据库文件
```

## 🚀 快速开始

### 环境要求
- Java 8 或更高版本
- Maven 3.6 或更高版本
- 现代浏览器（Chrome、Firefox、Safari等）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/po-todo-list.git
   cd po-todo-list
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问应用**
   - 打开浏览器访问：http://localhost:8081
   - 系统会自动初始化SQLite数据库
   - 首次使用请先注册账户

### 🎯 使用指南

1. **注册/登录**
   - 访问首页，点击"开始使用"
   - 选择"注册"创建新账户或"登录"现有账户

2. **管理任务**
   - 在任务页面添加新的待办事项
   - 点击✓标记任务完成
   - 点击⭐为重要任务添加星标
   - 点击✕删除不需要的任务

3. **查看统计**
   - 访问"数据分析"页面
   - 查看任务完成趋势图表
   - 了解个人效率统计

## 🔧 配置说明

### 数据库配置
项目使用SQLite作为数据库，配置文件位于 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    driver-class-name: org.sqlite.JDBC
    url: jdbc:sqlite:./data/potodo.db
```

### 端口配置
默认端口为8081，可在配置文件中修改：

```yaml
server:
  port: 8081
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 更新日志

### v1.0.0
- ✅ 基础任务管理功能
- ✅ 用户认证系统
- ✅ 数据统计分析
- ✅ 响应式UI设计
- ✅ SQLite数据库支持

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 作者

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/your-username)

## 🙏 致谢

- Spring Boot 团队提供的优秀框架
- Vue.js 社区的技术支持
- Chart.js 提供的图表解决方案

---

⭐ 如果这个项目对你有帮助，请给它一个星标！