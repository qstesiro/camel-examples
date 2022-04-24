# 编译
{
    # 正常运行
    alias mvn='mvn compile camel:run'

    # 调试运行
    alias mvn='rm -f /tmp/dbz-demo-123456.offset; rm -f /tmp/dbz-demo-123456.dbhistory; set -m; mvnDebug compile camel:run& pid=$!; jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=1025; kill -SIGTERM -- -$pid; set +m'

    # 编译组件
    cd ./components
    mvn install -pl camel-elasticsearch-rest -am -Dmaven.test.skip=true
    cd ../
}

# kafka
{
    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --list

    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --topic dbz-demo-123456.dbhistory \
                    --describe
    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --topic dbz-demo-123456.offset \
                    --describe

    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --topic dbz-demo-123456.dbhistory \
                    --delete
    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --topic dbz-demo-123456.offset \
                    --delete
}
