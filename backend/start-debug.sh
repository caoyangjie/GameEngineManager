#!/bin/bash
# 启动应用并显示详细错误信息

echo "正在启动应用..."
echo "================================"

cd "$(dirname "$0")"

# 使用 Maven 启动并显示所有输出
mvn spring-boot:run 2>&1 | tee startup.log

# 如果启动失败，显示最后50行日志
if [ $? -ne 0 ]; then
    echo ""
    echo "================================"
    echo "启动失败！最后50行日志："
    echo "================================"
    tail -50 startup.log
fi

