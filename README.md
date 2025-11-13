# 游戏引擎管理系统

基于 Spring Boot 和 Vue 3 的前后端分离管理系统，参考若依管理框架设计。

## 项目结构

```
GameEngineManager/
├── backend/          # 后端 Spring Boot 工程
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/gameengine/
│   │   │   │       ├── common/        # 通用工具类
│   │   │   │       ├── framework/     # 框架核心
│   │   │   │       └── system/        # 系统模块
│   │   │   └── resources/
│   │   │       ├── application.yml    # 主配置文件
│   │   │       └── application-druid.yml  # 数据源配置
│   │   └── test/
│   └── pom.xml
│
└── frontend/         # 前端 Vue 3 工程
    ├── src/
    │   ├── api/          # API 接口
    │   ├── assets/       # 静态资源
    │   ├── components/   # 公共组件
    │   ├── layout/       # 布局组件
    │   ├── router/       # 路由配置
    │   ├── store/        # 状态管理
    │   ├── styles/       # 全局样式
    │   ├── utils/        # 工具函数
    │   ├── views/        # 页面视图
    │   ├── App.vue       # 根组件
    │   └── main.js       # 入口文件
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 技术栈

### 后端
- **Spring Boot 3.2.0** - 核心框架
- **MyBatis Plus 3.5.5** - ORM 框架
- **Druid 1.2.20** - 数据库连接池
- **JWT 0.12.3** - 身份认证
- **Hutool 5.8.23** - Java 工具类库
- **FastJSON2 2.0.43** - JSON 处理

### 前端
- **Vue 3.4.0** - 前端框架
- **Vue Router 4.2.5** - 路由管理
- **Pinia 2.1.7** - 状态管理
- **Element Plus 2.4.4** - UI 组件库
- **Axios 1.6.2** - HTTP 客户端
- **Vite 5.0.0** - 构建工具

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 16+
- MySQL 5.7+

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 配置数据库：
   - 修改 `src/main/resources/application-druid.yml` 中的数据库连接信息
   - 创建数据库 `gameengine`

3. 编译运行：
```bash
mvn clean install
mvn spring-boot:run
```

或者使用 IDE 直接运行 `GameEngineManagerApplication.java`

4. 后端服务启动后，访问地址：`http://localhost:8080`

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

4. 前端服务启动后，访问地址：`http://localhost:3000`

## 功能特性

- ✅ 用户登录/登出
- ✅ 权限管理（待完善）
- ✅ 用户管理（待完善）
- ✅ 角色管理（待完善）
- ✅ 菜单管理（待完善）
- ✅ 前后端分离架构
- ✅ JWT 身份认证
- ✅ RESTful API 设计

## 开发说明

### 后端开发

- 控制器统一继承 `BaseController`
- 返回结果使用 `AjaxResult` 封装
- 实体类继承 `BaseEntity` 获得通用字段
- 使用 MyBatis Plus 进行数据库操作

### 前端开发

- 使用 Composition API 编写组件
- API 请求统一使用 `src/api/request.js` 封装的 axios 实例
- 路由配置在 `src/router/index.js`
- 状态管理使用 Pinia，定义在 `src/store/` 目录

## 项目配置

### 后端配置

主要配置文件：
- `application.yml` - 主配置文件
- `application-druid.yml` - 数据源配置

### 前端配置

主要配置文件：
- `vite.config.js` - Vite 构建配置
- `src/api/request.js` - Axios 请求配置

## 注意事项

1. 首次运行需要创建数据库并执行初始化 SQL（待提供）
2. 后端默认端口：8080
3. 前端默认端口：3000
4. 前端代理配置在 `vite.config.js` 中，将 `/api` 请求代理到后端

## 参考框架

本项目参考了若依管理框架（RuoYi）的设计思路和代码结构：
- 后端参考若依的 Spring Boot 版本
- 前端参考若依的 Vue 3 版本

## 许可证

MIT License

