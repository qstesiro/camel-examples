# 编译
{
    # 正常运行
    alias mvn='mvn compile camel:run'
    # 调试运行
    alias mvn='set -m; mvnDebug compile camel:run& pid=$!; jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=1025; kill -SIGTERM -- -$pid; set +m'
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
