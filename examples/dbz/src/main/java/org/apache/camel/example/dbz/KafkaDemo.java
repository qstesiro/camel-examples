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
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Message;

import org.apache.commons.dbcp2.*;
import javax.sql.DataSource;

/*
  基于CDC本地环境
 */

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class KafkaDemo {

    public void route() throws Exception {
        try (CamelContext camel = new DefaultCamelContext(getRegistry())) {
            camel.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() {
                        from(uri())
                            .routeId("kafka-demo")
                            // .log("${body}")
                            .process(e -> {
                                    Message msg = e.getMessage();
                                    if (msg != null) {
                                        System.out.printf("--- message type: %s\n", msg.getClass().toString());
                                        Object body = msg.getBody();
                                        if (body != null) {
                                            System.out.printf("--- body type: %s\n", body.getClass().toString());
                                        } else {
                                            System.out.printf("--- body null\n");
                                        }
                                    } else {
                                        System.out.printf("message null\n");
                                    }
                                })
                            .unmarshal().json()
                            // .convertBodyTo(LinkedHashMap.class)
                            .process(e -> {
                                    Message msg = e.getMessage();
                                    if (msg != null) {
                                        System.out.printf("--- message type: %s\n", msg.getClass().toString());
                                        Object body = msg.getBody();
                                        if (body != null) {
                                            System.out.printf("--- body type: %s\n", body.getClass().toString());
                                        } else {
                                            System.out.printf("--- body null\n");
                                        }
                                    } else {
                                        System.out.printf("message null\n");
                                    }
                                })
                            .log("${body[source][query]}")
                            // .log("    on the topic ${headers[kafka.TOPIC]}")
                            // .log("    on the partition ${headers[kafka.PARTITION]}")
                            // .log("    with the offset ${headers[kafka.OFFSET]}")
                            // .log("    with the key ${headers[kafka.KEY]}")
                            ;
                    }
                });

            camel.start();
            Thread.sleep(10_100_000);
            camel.stop();
        }
    }

    private String uri() {
        return "kafka:demo-43585133"
            + "?brokers=localhost:9092"
            + "&autoOffsetReset=earliest";
    }

    // 本地环境
    private final static String TO_USER = "root";
    private final static String TO_PASSWORD = "debezium";
    private final static String URL = "jdbc:mysql://localhost:3306/console?characterEncoding=utf8";

    // 测试环境
    // private final static String TO_USER = "console";
    // private final static String TO_PASSWORD = "suMuCaSu1e";
    // private final static String URL = "jdbc:mysql://10.138.228.243:3306/console?characterEncoding=utf8";

    private DefaultRegistry getRegistry() {
        DefaultRegistry reg = new DefaultRegistry();
        reg.bind("demo", getDataSource());
        return reg;
    }

    private DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername(TO_USER);
        ds.setPassword(TO_PASSWORD);
        ds.setUrl(URL);
        return ds;
    }
}
