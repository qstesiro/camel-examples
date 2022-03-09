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
mysql -h127.0.0.1 -P3306 -udebezium -pdbz -D inventory
*/

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class DbzDemo {

    private static final Logger LOG = LoggerFactory.getLogger(DbzDemo.class);

    public static void main(String[] args) throws Exception {

        LOG.debug("About to run Debezium integration...");

        try (CamelContext camel = new DefaultCamelContext()) {
            camel.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() {
                        from("debezium-mysql:dbz-demo?"
                             + "databaseServerName=dbz-demo-123456"
                             + "&databaseServerId=123456"
                             + "&databaseHostname=localhost"
                             + "&databaseUser=debezium"
                             + "&databasePassword=dbz"
                             + "&databaseIncludeList=inventory"
                             + "&tableIncludeList=inventory.customers"
                             // + "&offsetStorage=org.apache.kafka.connect.storage.MemoryOffsetBackingStore"
                             // + "&databaseHistory=org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
                             + "&offsetStorageFileName=/home/qstesiro/github.com/qstesiro/camel/camel-examples/examples/dbz/offset"
                             + "&databaseHistoryFileFilename=/home/qstesiro/github.com/qstesiro/camel/camel-examples/examples/dbz/dbhistory")
                            // .log("Incoming message ${body} with headers ${headers}");
                            .log("Incoming message with headers");
                    }
                });
            camel.start();
            Thread.sleep(10_100_000);
            camel.stop();
        }
    }
}
