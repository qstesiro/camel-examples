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
