# RBAC 权限管理体系说明

## 概述

已为游戏引擎管理系统创建完整的 RBAC（基于角色的访问控制）体系，包括用户、角色、菜单的完整管理功能。

## 数据库表结构

### 核心表

1. **sys_user** - 用户表
   - 存储用户基本信息、登录信息
   - 支持密码加密（BCrypt）

2. **sys_role** - 角色表
   - 存储角色信息、权限范围

3. **sys_menu** - 菜单表
   - 存储菜单/权限信息
   - 支持树形结构（父子菜单）

4. **sys_user_role** - 用户角色关联表
   - 多对多关系：用户可以有多个角色

5. **sys_role_menu** - 角色菜单关联表
   - 多对多关系：角色可以有多个菜单权限

## 代码结构

### 实体类（Entity）

- `SysUser` - 用户实体
- `SysRole` - 角色实体
- `SysMenu` - 菜单实体
- `SysUserRole` - 用户角色关联实体
- `SysRoleMenu` - 角色菜单关联实体

### 数据访问层（Mapper）

- `SysUserMapper` - 用户数据访问
- `SysRoleMapper` - 角色数据访问
- `SysMenuMapper` - 菜单数据访问
- `SysUserRoleMapper` - 用户角色关联数据访问
- `SysRoleMenuMapper` - 角色菜单关联数据访问

### 业务逻辑层（Service）

- `ISysUserService` / `SysUserServiceImpl` - 用户业务逻辑
  - 用户登录验证
  - 用户信息查询
  - 角色和权限查询
  - 登录信息记录

- `ISysMenuService` / `SysMenuServiceImpl` - 菜单业务逻辑
  - 菜单树查询
  - 路由构建

### 控制器层（Controller）

- `LoginController` - 登录相关接口
  - `POST /login` - 用户登录
  - `GET /getInfo` - 获取用户信息（包含角色和权限）
  - `POST /logout` - 退出登录

- `SysMenuController` - 菜单相关接口
  - `GET /system/menu/getRouters` - 获取路由菜单

### 工具类

- `JwtUtils` - JWT Token 工具类
  - Token 生成
  - Token 解析
  - Token 验证

- `SecurityUtils` - 安全工具类
  - 密码加密（BCrypt）
  - 密码验证

- `PasswordGenerator` - 密码生成工具（用于生成初始密码）

### 安全配置

- `JwtAuthenticationTokenFilter` - JWT 认证过滤器
  - 自动从请求头提取 Token
  - 验证 Token 有效性
  - 设置 Spring Security 认证上下文

- `SecurityConfig` - Spring Security 配置
  - 配置 JWT 过滤器
  - 配置接口访问权限
  - 允许登录、Swagger、Druid 等路径无需认证

## API 接口说明

### 1. 用户登录

**接口**: `POST /login`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应**:
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9..."
  }
}
```

### 2. 获取用户信息

**接口**: `GET /getInfo`

**请求头**:
```
Authorization: Bearer {token}
```

**响应**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "user": {
      "userId": 1,
      "userName": "admin",
      "nickName": "管理员",
      ...
    },
    "roles": ["admin"],
    "permissions": ["system:user:list", "system:user:add", ...]
  }
}
```

### 3. 获取路由菜单

**接口**: `GET /system/menu/getRouters`

**请求头**:
```
Authorization: Bearer {token}
```

**响应**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "menuId": 1,
      "menuName": "系统管理",
      "path": "/system",
      "children": [...]
    }
  ]
}
```

### 4. 退出登录

**接口**: `POST /logout`

**响应**:
```json
{
  "code": 200,
  "msg": "退出成功"
}
```

## 初始化数据

数据库迁移脚本 `V1__Initial_schema.sql` 已包含：

1. **管理员用户**
   - 用户名: `admin`
   - 密码: `admin123`（BCrypt 加密）
   - 状态: 正常

2. **管理员角色**
   - 角色名: `超级管理员`
   - 角色标识: `admin`

3. **用户角色关联**
   - 自动关联 admin 用户和 admin 角色

## 使用说明

### 1. 生成密码哈希

如果需要生成新的密码哈希，可以运行：

```java
// 运行 PasswordGenerator.main() 方法
// 或直接使用 SecurityUtils.encryptPassword("your_password")
```

### 2. 前端集成

前端登录流程：

1. 调用 `POST /login` 接口，获取 token
2. 将 token 存储到 localStorage 或 Vuex/Pinia
3. 在后续请求的请求头中添加：`Authorization: Bearer {token}`
4. 调用 `GET /getInfo` 获取用户信息和权限
5. 调用 `GET /system/menu/getRouters` 获取路由菜单

### 3. 权限验证

- 所有需要认证的接口都会自动验证 JWT Token
- Token 过期时间在 `application.yml` 中配置（默认 30 分钟）
- 前端应在 Token 过期前刷新或重新登录

## 配置说明

### application.yml

```yaml
token:
  header: Authorization          # Token 请求头名称
  secret: your-secret-key        # JWT 密钥
  expireTime: 30                 # Token 有效期（分钟）
```

## 注意事项

1. **密码加密**: 所有密码都使用 BCrypt 加密，不可逆
2. **Token 安全**: JWT Token 包含用户ID和用户名，请妥善保管密钥
3. **权限控制**: 当前版本实现了基础的认证，权限验证逻辑可根据业务需求扩展
4. **菜单权限**: 菜单权限通过 `perms` 字段存储，格式如：`system:user:list`

## 后续扩展

可以继续扩展的功能：

1. **用户管理**: 用户 CRUD、密码重置
2. **角色管理**: 角色 CRUD、权限分配
3. **菜单管理**: 菜单 CRUD、动态路由
4. **权限拦截**: 基于注解的权限验证（如 `@PreAuthorize`）
5. **操作日志**: 记录用户操作日志
6. **数据权限**: 基于角色的数据范围控制

## 测试

启动应用后，可以通过以下方式测试：

1. **Swagger UI**: `http://localhost:18080/swagger-ui.html`
2. **Knife4j UI**: `http://localhost:18080/doc.html`
3. **直接调用 API**: 使用 Postman 或 curl

测试登录：
```bash
curl -X POST http://localhost:18080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

