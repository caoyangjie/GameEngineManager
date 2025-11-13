# 启动问题排查指南

## Maven exec 插件执行失败

### 错误信息
```
Failed to execute goal org.codehaus.mojo:exec-maven-plugin:3.1.0:exec (default-cli) on project game-engine-manager: Command execution failed.: Process exited with an error: 1 (Exit value: 1)
```

### 可能的原因和解决方案

#### 1. Spring Security 配置问题

**问题**：Spring Security 默认拦截所有请求，但没有正确配置。

**解决**：已创建 `SecurityConfig.java` 配置文件，允许所有请求访问（开发环境）。

#### 2. Redis 连接失败

**问题**：如果 Redis 未启动，应用可能启动失败。

**解决**：已在 `application.yml` 中临时禁用 Redis 自动配置。如果不需要 Redis，可以保持此配置。

如果需要使用 Redis：
1. 启动 Redis 服务
2. 取消注释 `application.yml` 中的 Redis 配置
3. 移除 `autoconfigure.exclude` 中的 Redis 相关配置

#### 3. 数据库连接失败

**问题**：数据库连接配置错误或数据库未启动。

**检查步骤**：
1. 确认数据库服务已启动
2. 检查 `application-druid.yml` 中的连接信息：
   - URL: `jdbc:mysql://localhost:3306/gameengine`
   - 用户名: `root`
   - 密码: `password`
3. 确认数据库 `gameengine` 已创建

**测试连接**：
```bash
mysql -h localhost -u root -p -e "USE gameengine; SELECT 1;"
```

#### 4. Flyway 迁移失败

**问题**：数据库迁移脚本执行失败。

**解决步骤**：
1. 查看详细错误日志
2. 检查迁移脚本语法
3. 如果迁移失败，可以临时禁用 Flyway：
   ```yaml
   spring:
     flyway:
       enabled: false
   ```

#### 5. 端口被占用

**问题**：18080 端口已被其他程序占用。

**检查**：
```bash
netstat -tuln | grep 18080
# 或
lsof -i :18080
```

**解决**：修改 `application.yml` 中的端口号。

### 诊断步骤

1. **使用调试脚本启动**：
   ```bash
   cd backend
   ./start-debug.sh
   ```

2. **查看完整错误日志**：
   启动脚本会保存日志到 `startup.log` 文件。

3. **使用 Maven 直接启动**：
   ```bash
   mvn spring-boot:run
   ```

4. **检查日志文件**：
   ```bash
   tail -f ~/logs/gameengine/gameengine.log
   ```

### 常见错误和解决方案

#### 错误：数据库连接超时
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```
**解决**：检查数据库服务是否启动，连接信息是否正确。

#### 错误：表已存在
```
Table 'xxx' already exists
```
**解决**：Flyway 迁移脚本使用了 `CREATE TABLE IF NOT EXISTS`，应该不会出现此错误。如果出现，检查数据库状态。

#### 错误：Spring Security 配置错误
```
java.lang.IllegalStateException: Cannot apply org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
```
**解决**：已创建 `SecurityConfig.java`，应该已解决。

### 快速修复

如果以上方法都不行，可以尝试：

1. **临时禁用所有非必需功能**：
   ```yaml
   spring:
     flyway:
       enabled: false
     autoconfigure:
       exclude:
         - org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
   ```

2. **使用最小配置启动**：
   注释掉所有非必需的配置，只保留基本的 Web 功能。

3. **检查依赖冲突**：
   ```bash
   mvn dependency:tree | grep conflict
   ```

### 获取帮助

如果问题仍然存在，请提供：
1. 完整的启动日志（最后 100 行）
2. 错误堆栈信息
3. 数据库和 Redis 状态
4. Java 和 Maven 版本信息

