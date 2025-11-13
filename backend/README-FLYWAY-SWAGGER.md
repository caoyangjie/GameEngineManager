# Flyway 和 Swagger 使用说明

## Flyway 数据库版本管理

### 功能说明

Flyway 是一个数据库版本控制和迁移工具，用于管理数据库结构的变更。

### 配置说明

在 `application.yml` 中已配置 Flyway：

```yaml
spring:
  flyway:
    enabled: true                    # 是否启用 Flyway
    locations: classpath:db/migration  # 迁移脚本位置
    table: flyway_schema_history      # 迁移历史表名
    baseline-on-migrate: true        # 首次迁移时创建基线
    validate-on-migrate: true        # 验证迁移脚本
```

### 迁移脚本位置

迁移脚本存放在：`src/main/resources/db/migration/`

### 命名规则

迁移脚本必须遵循以下命名规则：

```
V{version}__{description}.sql
```

示例：
- `V1__Initial_schema.sql` - 初始数据库结构
- `V1_1_0__Add_user_table.sql` - 添加用户表
- `V1_2_0__Add_role_permission.sql` - 添加角色权限表

### 使用步骤

1. **创建迁移脚本**：
   - 在 `db/migration` 目录下创建新的 SQL 文件
   - 文件名必须遵循命名规则
   - 版本号必须递增

2. **执行迁移**：
   - 启动应用时，Flyway 会自动执行未执行的迁移脚本
   - 或者使用 Maven 命令：`mvn flyway:migrate`

3. **查看迁移历史**：
   - 迁移历史记录在数据库的 `flyway_schema_history` 表中

### 注意事项

- ⚠️ 已执行的迁移脚本不要修改
- ⚠️ 迁移脚本应该是幂等的（可以重复执行）
- ⚠️ 使用 `IF NOT EXISTS` 或 `CREATE TABLE IF NOT EXISTS` 等语句
- ⚠️ 避免在迁移脚本中使用 `DROP` 语句（除非必要）

## Swagger API 文档

### 功能说明

Swagger 用于自动生成和展示 RESTful API 文档。

### 访问地址

启动应用后，可以通过以下地址访问 API 文档：

- **默认 Swagger UI**：`http://localhost:18080/swagger-ui.html`
- **Bootstrap UI（推荐）**：`http://localhost:18080/doc.html`

### Bootstrap UI 特性

Bootstrap UI 版本相比默认版本有以下优势：

1. ✅ 更美观的界面设计
2. ✅ 更好的移动端适配
3. ✅ 更丰富的功能展示
4. ✅ 更好的用户体验

### 使用 Swagger 注解

在 Controller 中使用 Swagger 注解：

```java
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    
    @ApiOperation(value = "获取用户列表", notes = "分页查询用户信息")
    @GetMapping("/list")
    public AjaxResult list() {
        // ...
    }
    
    @ApiOperation(value = "获取用户详情")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        // ...
    }
}
```

### 常用注解

- `@Api` - 标注在 Controller 类上，用于描述整个 Controller
- `@ApiOperation` - 标注在方法上，用于描述接口
- `@ApiParam` - 标注在参数上，用于描述参数
- `@ApiModel` - 标注在实体类上，用于描述实体
- `@ApiModelProperty` - 标注在实体属性上，用于描述属性

### 安全配置

Swagger 已配置 JWT Token 认证：

1. 在 Swagger UI 中点击右上角的 "Authorize" 按钮
2. 输入 Token：`Bearer {your-token}`
3. 点击 "Authorize" 完成认证

### 配置说明

Swagger 配置类：`SwaggerConfig.java`

主要配置：
- API 文档标题和描述
- 扫描的包路径：`com.gameengine`
- 安全认证方式：JWT Token
- 访问路径：`/swagger-ui.html` 和 `/doc.html`

## 快速开始

### 1. 启动应用

```bash
cd backend
mvn spring-boot:run
```

### 2. 访问 Swagger 文档

打开浏览器访问：`http://localhost:18080/doc.html`

### 3. 查看数据库迁移

启动应用后，Flyway 会自动执行迁移脚本，可以在数据库中查看 `flyway_schema_history` 表。

## 常见问题

### Q: Flyway 迁移失败怎么办？

A: 检查迁移脚本的语法是否正确，确保版本号递增，不要修改已执行的脚本。

### Q: Swagger UI 无法访问？

A: 检查应用是否正常启动，确认端口号是否正确（默认 18080）。

### Q: 如何禁用 Swagger？

A: 在 `application.yml` 中设置 `swagger.enabled: false`，或在生产环境中排除 Swagger 依赖。

### Q: Bootstrap UI 和默认 UI 有什么区别？

A: Bootstrap UI 使用 Bootstrap 框架，界面更美观，功能更丰富。默认 UI 是 Swagger 官方提供的标准界面。

