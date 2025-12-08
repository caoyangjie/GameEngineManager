# 编译版本
mvn clean package -DskipTests=true

# 复制到 /opt/game-engine-manager
awscp target/game-engine-manager.jar /data/apps/GameManager/game-engine-manager-1.0.0.jar