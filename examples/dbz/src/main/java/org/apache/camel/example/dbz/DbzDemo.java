/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.dbz;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
  docker run \
  --name mysql \
  --network host \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=debezium \
  -e MYSQL_USER=mysqluser \
  -e MYSQL_PASSWORD=mysqluser \
  --rm -d \
  debezium/example-mysql:1.7
  mysql -h127.0.0.1 -P3306 -umysqluser -pmysqluser -D inventory
*/

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class DbzDemo {

    // private static final Logger LOG = LoggerFactory.getLogger(DbzDemo.class);

    public static void main(String[] args) throws Exception {
        new DbzDemo().route();
    }

    private void route() throws Exception {
        try (CamelContext camel = new DefaultCamelContext()) {
            camel.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() {
                        from("debezium-mysql:dbz-demo?"
                             //
                             + "databaseServerName=dbz-demo-123456"
                             + "&databaseServerId=123456"
                             + "&databaseHostname=localhost"
                             + "&databasePort=3306"
                             + "&databaseUser=debezium"
                             + "&databasePassword=dbz"
                             //
                             + "&databaseIncludeList=inventory"
                             + "&tableIncludeList=inventory.customers"
                             + localStorage())
                            // + kafkaStorage())
                            .filter(simple("${header.CamelDebeziumIdentifier} == 'dbz-demo-123456'")).stop()
                            .end()
                            .process(e -> {
                                    Message msg = e.getMessage();
                                    if (msg != null) {
                                        System.out.printf("--- headers \n");
                                        printHeaders(msg.getHeaders());
                                        System.out.printf("--- message type: %s\n", msg.getClass().toString());
                                        Object body = msg.getBody();
                                        if (body != null) {
                                            System.out.printf("--- body type: %s\n", body.getClass().toString());
                                        } else {
                                            System.out.printf("--- body null\n");
                                        }
                                    }
                                })
                            .convertBodyTo(Map.class)
                            // .marshal().json(JsonLibrary.Fastjson)
                            .log("Event received from Debezium : ${body}")
                            .log("    with this identifier ${headers.CamelDebeziumIdentifier}")
                            .log("    with these source metadata ${headers.CamelDebeziumSourceMetadata}")
                            .log("    the event occured upon this operation '${headers.CamelDebeziumSourceOperation}'")
                            .log("    on this database '${headers.CamelDebeziumSourceMetadata[db]}' and this table '${headers.CamelDebeziumSourceMetadata[table]}'")
                            .log("    with the key ${headers.CamelDebeziumKey}")
                            .log("    the previous value is ${headers.CamelDebeziumBefore}")
                            .log("    the ddl sql text is ${headers.CamelDebeziumDdlSQL}")
                            .routeId("dbz-demo");
                    }
                });

            camel.start();
            Thread.sleep(10_100_000);
            camel.stop();
        }
    }

    private String localStorage() {
        return "&offsetStorageFileName=/tmp/offset"
            // offsetFlushIntervalMs字段存在bug,文档中默认1m但实际不生效
            + "&offsetFlushIntervalMs=1000"
            + "&databaseHistoryFileFilename=/tmp/dbhistory";
    }

    private String kafkaStorage() {
        return "&additionalProperties.bootstrap.servers=127.0.0.1:9092"
            // "&additionalProperties.bootstrap.servers=localhost:9092"
            // + "&configuration.bootstrap.servers=10.138.16.188:9092"
            + "&offsetStorage=org.apache.kafka.connect.storage.KafkaOffsetBackingStore"
            + "&offsetStorageTopic=dbz-demo-123456.offset"
            + "&offsetStoragePartitions=3"
            + "&offsetStorageReplicationFactor=3"
            + "&offsetFlushIntervalMs=1000"
            + "&offsetCommitTimeoutMs=1000"
            //
            + "&databaseHistoryKafkaBootstrapServers=127.0.0.1:9092"
            + "&databaseHistory=io.debezium.relational.history.KafkaDatabaseHistory"
            + "&databaseHistoryKafkaTopic=dbz-demo-123456.dbhistory";
        // + "&offsetStorage=org.apache.kafka.connect.storage.MemoryOffsetBackingStore";

        // for debug ???
        // return "additionalProperties.bootstrap.servers=127.0.0.1:9092";
    }

    private void printHeaders(Map<String, Object> map) {
        for (String key : map.keySet()) {
            System.out.printf("%s: %s\n", key, map.get(key));
        }
    }
}
