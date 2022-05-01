# 编译
{
    # 正常运行
    alias mvn='mvn compile camel:run'

    # 调试运行(java-11)
    alias mvn='set -m; mvnDebug compile camel:run& pid=$!; jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=1025; kill -SIGTERM -- -$pid; set +m'

    # 编译组件(mvn-3.6.3)
    mvn install -pl camel-elasticsearch-rest -am -Dmaven.test.skip=true
    mvn install -pl camel-kafka -am -Dmaven.test.skip=true
}

# kafka
{
    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --list

    kafka-topics.sh --bootstrap-server 10.138.16.188:9092 \
                    --topic mysql-prod.console.org \
                    --describe

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

    kafka-consumer-groups.sh --bootstrap-server 10.138.16.188:9092 \
                             --group 2021-1231-1035-orgs-prod \
                             --describe

    kafka-consumer-groups.sh --bootstrap-server 10.138.16.188:9092 \
                             --group 2021-1231-1035-orgs-prod \
                             --topic mysql-prod.console.org:1 \
                             --to-offset 51257 \
                             --reset-offsets \
                             --execute

    kafka-console-consumer.sh --bootstrap-server 10.138.16.188:9092 \
                              --topic __consumer_offsets \
                              --property print.timestamp=true \
                              --property print.offset=true \
                              --property print.key=true \
                              --property print.value=true \
                              --from-beginning
}

2022-05-01 12:28:52,554 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.ticket.renew.window.factor' was supplied but isn't a known config.
2022-05-01 12:28:52,554 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.kinit.cmd' was supplied but isn't a known config.
2022-05-01 12:28:52,555 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'specific.avro.reader' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.ticket.renew.jitter' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.trustmanager.algorithm' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.keystore.type' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.min.time.before.relogin' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.endpoint.identification.algorithm' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.protocol' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.enabled.protocols' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.truststore.type' was supplied but isn't a known config.
2022-05-01 12:28:52,556 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.keymanager.algorithm' was supplied but isn't a known config.
2022-05-01 12:28:52,558 [od.console.org]] INFO  AppInfoParser                  - Kafka version: 2.8.1
2022-05-01 12:28:52,558 [od.console.org]] INFO  AppInfoParser                  - Kafka commitId: 839b886f9b732b15
2022-05-01 12:28:52,559 [od.console.org]] INFO  AppInfoParser                  - Kafka startTimeMs: 1651379332556
2022-05-01 12:28:52,564 [od.console.org]] INFO  ResumeStrategyFactory          - Using NO-OP resume strategy
2022-05-01 12:28:52,564 [od.console.org]] INFO  KafkaFetchRecords              - Subscribing mysql-prod.console.org-Thread 0 to topic mysql-prod.console.org
2022-05-01 12:28:52,566 [od.console.org]] INFO  KafkaConsumer                  - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Subscribed to topic(s): mysql-prod.console.org
2022-05-01 12:28:53,501 [od.console.org]] INFO  Metadata                       - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Cluster ID: 9uLENPNJSXWUUSXKxN_OoA
2022-05-01 12:28:53,505 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Discovered group coordinator 10.138.16.190:9092 (id: 2147483457 rack: null)
2022-05-01 12:28:53,519 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] (Re-)joining group
2022-05-01 12:28:53,790 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] (Re-)joining group
2022-05-01 12:28:53,858 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Successfully joined group with generation Generation{generationId=18, memberId='consumer-hmm-demo-1-e439492d-96e7-48ae-8a11-aa9fdc5407a9', protocol='range'}
2022-05-01 12:28:53,862 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Finished assignment for group at generation 18: {consumer-hmm-demo-1-e439492d-96e7-48ae-8a11-aa9fdc5407a9=Assignment(partitions=[mysql-prod.console.org-0, mysql-prod.console.org-1, mysql-prod.console.org-2])}
2022-05-01 12:28:53,943 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Successfully synced group in generation Generation{generationId=18, memberId='consumer-hmm-demo-1-e439492d-96e7-48ae-8a11-aa9fdc5407a9', protocol='range'}
2022-05-01 12:28:53,943 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Notifying assignor about the new Assignment(partitions=[mysql-prod.console.org-0, mysql-prod.console.org-1, mysql-prod.console.org-2])
2022-05-01 12:28:53,947 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Adding newly assigned partitions: mysql-prod.console.org-2, mysql-prod.console.org-1, mysql-prod.console.org-0
2022-05-01 12:28:54,047 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Setting offset for partition mysql-prod.console.org-2 to the committed offset FetchPosition{offset=51799, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[10.138.16.188:9092 (id: 188 rack: null)], epoch=0}}
2022-05-01 12:28:54,047 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Setting offset for partition mysql-prod.console.org-1 to the committed offset FetchPosition{offset=51221, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[10.138.16.191:9092 (id: 191 rack: null)], epoch=0}}
2022-05-01 12:28:54,047 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-hmm-demo-1, groupId=hmm-demo] Setting offset for partition mysql-prod.console.org-0 to the committed offset FetchPosition{offset=51370, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[10.138.16.190:9092 (id: 190 rack: null)], epoch=0}}


2022-05-01 12:30:40,054 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.ticket.renew.window.factor' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.kinit.cmd' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'specific.avro.reader' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.ticket.renew.jitter' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.trustmanager.algorithm' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.keystore.type' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'sasl.kerberos.min.time.before.relogin' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.endpoint.identification.algorithm' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.protocol' was supplied but isn't a known config.
2022-05-01 12:30:40,055 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.enabled.protocols' was supplied but isn't a known config.
2022-05-01 12:30:40,056 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.truststore.type' was supplied but isn't a known config.
2022-05-01 12:30:40,056 [od.console.org]] WARN  ConsumerConfig                 - The configuration 'ssl.keymanager.algorithm' was supplied but isn't a known config.
2022-05-01 12:30:40,060 [od.console.org]] INFO  AppInfoParser                  - Kafka version: 2.8.1
2022-05-01 12:30:40,061 [od.console.org]] INFO  AppInfoParser                  - Kafka commitId: 839b886f9b732b15
2022-05-01 12:30:40,061 [od.console.org]] INFO  AppInfoParser                  - Kafka startTimeMs: 1651379440056
2022-05-01 12:30:40,072 [od.console.org]] INFO  ResumeStrategyFactory          - Using NO-OP resume strategy
2022-05-01 12:30:40,072 [od.console.org]] INFO  KafkaFetchRecords              - Subscribing mysql-prod.console.org-Thread 0 to topic mysql-prod.console.org
2022-05-01 12:30:40,075 [od.console.org]] INFO  KafkaConsumer                  - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Subscribed to topic(s): mysql-prod.console.org
2022-05-01 12:30:40,935 [od.console.org]] INFO  Metadata                       - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Cluster ID: 9uLENPNJSXWUUSXKxN_OoA
2022-05-01 12:30:40,950 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Discovered group coordinator 10.138.16.188:9092 (id: 2147483459 rack: null)
2022-05-01 12:30:40,954 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] (Re-)joining group
2022-05-01 12:30:41,196 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] (Re-)joining group
2022-05-01 12:30:41,239 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Successfully joined group with generation Generation{generationId=36, memberId='consumer-2021-1231-1035-orgs-prod-1-e33ba211-243b-4069-b940-c2517bc3783d', protocol='range'}
2022-05-01 12:30:41,245 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Finished assignment for group at generation 36: {consumer-2021-1231-1035-orgs-prod-1-e33ba211-243b-4069-b940-c2517bc3783d=Assignment(partitions=[mysql-prod.console.org-0, mysql-prod.console.org-1, mysql-prod.console.org-2])}
2022-05-01 12:30:41,290 [od.console.org]] INFO  AbstractCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Successfully synced group in generation Generation{generationId=36, memberId='consumer-2021-1231-1035-orgs-prod-1-e33ba211-243b-4069-b940-c2517bc3783d', protocol='range'}
2022-05-01 12:30:41,298 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Notifying assignor about the new Assignment(partitions=[mysql-prod.console.org-0, mysql-prod.console.org-1, mysql-prod.console.org-2])
2022-05-01 12:30:41,303 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Adding newly assigned partitions: mysql-prod.console.org-2, mysql-prod.console.org-1, mysql-prod.console.org-0
2022-05-01 12:30:41,430 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Setting offset for partition mysql-prod.console.org-2 to the committed offset FetchPosition{offset=51812, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[10.138.16.188:9092 (id: 188 rack: null)], epoch=0}}
2022-05-01 12:30:41,451 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Setting offset for partition mysql-prod.console.org-1 to the committed offset FetchPosition{offset=49349, offsetEpoch=Optional[2], currentLeader=LeaderAndEpoch{leader=Optional.empty, epoch=2}}
2022-05-01 12:30:41,451 [od.console.org]] INFO  ConsumerCoordinator            - [Consumer clientId=consumer-2021-1231-1035-orgs-prod-1, groupId=2021-1231-1035-orgs-prod] Setting offset for partition mysql-prod.console.org-0 to the committed offset FetchPosition{offset=51371, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[10.138.16.190:9092 (id: 190 rack: null)], epoch=0}}
