# Flyway 数据库迁移脚本

## 目录说明

本目录用于存放 Flyway 数据库迁移脚本。

## 命名规则

Flyway 迁移脚本必须遵循以下命名规则：

```
V{version}__{description}.sql
```

- `V` - 版本前缀（固定）
- `{version}` - 版本号，格式：主版本号.次版本号.修订号（例如：1.0.0 或 1_0_0）
- `__` - 分隔符（两个下划线）
- `{description}` - 描述信息（使用下划线分隔单词）
- `.sql` - 文件扩展名

## 示例

- `V1__Initial_schema.sql` - 初始数据库结构
- `V1_1_0__Add_user_table.sql` - 添加用户表
- `V1_2_0__Add_role_permission.sql` - 添加角色权限表

## 使用说明

1. **创建新迁移脚本**：
   - 在 `db/migration` 目录下创建新的 SQL 文件
   - 文件名必须遵循命名规则
   - 版本号必须递增

2. **执行迁移**：
   - 启动应用时，Flyway 会自动执行未执行的迁移脚本
   - 或者使用 Maven 命令：`mvn flyway:migrate`

3. **回滚**：
   - Flyway 不支持自动回滚
   - 需要手动创建新的迁移脚本来修复

## 注意事项

- ⚠️ 已执行的迁移脚本不要修改
- ⚠️ 迁移脚本应该是幂等的（可以重复执行）
- ⚠️ 使用 `IF NOT EXISTS` 或 `CREATE TABLE IF NOT EXISTS` 等语句
- ⚠️ 避免在迁移脚本中使用 `DROP` 语句（除非必要）

## 迁移历史

迁移历史记录在数据库的 `flyway_schema_history` 表中。

