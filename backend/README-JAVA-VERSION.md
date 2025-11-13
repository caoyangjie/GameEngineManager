# Java 版本问题解决方案

## 问题说明

当前系统检测到您使用的是 **Java 8**，但项目默认配置需要 **Java 17**（Spring Boot 3.2.0 要求）。

## 解决方案

### 方案一：升级到 Java 17（推荐）

**优点：**
- 可以使用最新的 Spring Boot 3.x 特性
- 性能更好，功能更强大
- 长期支持

**步骤：**

1. 安装 Java 17：
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# 或者使用 SDKMAN
curl -s "https://get.sdkman.io" | bash
sdk install java 17.0.2-tem
```

2. 设置 JAVA_HOME：
```bash
# 查找 Java 17 安装路径
sudo update-alternatives --config java

# 设置环境变量（添加到 ~/.bashrc 或 ~/.zshrc）
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

3. 验证版本：
```bash
java -version
# 应该显示 java version "17.x.x"
```

4. 重新编译：
```bash
mvn clean install -DskipTests=true
```

### 方案二：使用 Java 8 兼容版本（临时方案）

如果暂时无法升级 Java，可以使用兼容 Java 8 的配置：

1. 备份当前 pom.xml：
```bash
cp pom.xml pom-java17.xml
```

2. 使用 Java 8 兼容版本：
```bash
cp pom-java8.xml pom.xml
```

3. 重新编译：
```bash
mvn clean install -DskipTests=true
```

**注意：** Java 8 兼容版本使用 Spring Boot 2.7.18，功能上会有一些差异。

## 推荐做法

建议升级到 Java 17，因为：
- Spring Boot 3.x 不再支持 Java 8
- Java 8 已经停止更新（2023年3月）
- Java 17 是 LTS（长期支持）版本

