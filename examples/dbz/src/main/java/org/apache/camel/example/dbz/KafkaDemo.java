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
            camel.addRoutes(new SimpleRoute());
            // camel.addRoutes(new GroovyRoute());
            camel.start();
            Thread.sleep(10_100_000);
            camel.stop();
        }
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

    class SimpleRoute extends RouteBuilder {

        @Override
        public void configure() {
            from(uri())
                .routeId("kafka-demo")
                // .log("${body}")
                // .process(e -> {
                //         Message msg = e.getMessage();
                //         if (msg != null) {
                //             System.out.printf("--- message type: %s\n", msg.getClass().toString());
                //             Object body = msg.getBody();
                //             if (body != null) {
                //                 System.out.printf("--- body type: %s\n", body.getClass().toString());
                //             } else {
                //                 System.out.printf("--- body null\n");
                //             }
                //         } else {
                //             System.out.printf("message null\n");
                //         }
                //     })
                .unmarshal().json()
                // .convertBodyTo(LinkedHashMap.class)
                // .process(e -> {
                //         Message msg = e.getMessage();
                //         if (msg != null) {
                //             System.out.printf("--- message type: %s\n", msg.getClass().toString());
                //             Object body = msg.getBody();
                //             if (body != null) {
                //                 System.out.printf("--- body type: %s\n", body.getClass().toString());
                //             } else {
                //                 System.out.printf("--- body null\n");
                //             }
                //         } else {
                //             System.out.printf("message null\n");
                //         }
                //     })
                .log("${body}")
                // .choice()
                //     .when(simple(ddlOp()))
                //         .setBody(simple(ddl()))
                //         .log("${body}")
                // //.to("jdbc:demo")
                //     .when(simple(selectOp()))
                //         .setBody(simple(select()))
                //         .log("${body}")
                // // .to("jdbc:demo")
                //     .when(simple(dmlOp()))
                //         .setBody(simple(dml()))
                //         .log("${body}")
                // // .to("jdbc:demo")
                //     .otherwise()
                //         .log("discard --- ${body}")
                // .log("${body}")
                // .log("    on the topic ${headers[kafka.TOPIC]}")
                // .log("    on the partition ${headers[kafka.PARTITION]}")
                // .log("    with the offset ${headers[kafka.OFFSET]}")
                // .log("    with the key ${headers[kafka.KEY]}")
                ;
        }

        protected String uri() {
            return "kafka:mysql-prod.console.org"
                + "?brokers=10.138.16.188:9092"
                + "&groupId=2021-1231-1035-orgs-prod"
                + "&autoOffsetReset=earliest"
                + "&sessionTimeoutMs=10000"
                + "&heartbeatIntervalMs=3000"
                + "&pollTimeoutMs=1000"
                + "&maxPollRecords=1";
            // return "kafka:mysql-prod.console.org"
            //     + "?brokers=10.138.16.188:9092"
            //     + "&groupId=hmm-demo"
            //     + "&autoOffsetReset=earliest"
            //     + "&sessionTimeoutMs=10000"
            //     + "&heartbeatIntervalMs=3000"
            //     + "&pollTimeoutMs=1000"
            //     + "&maxPollRecords=1";
        }

        protected String ddlOp() {
            return "${body[ddl]} != null && ${body[source][table]} == 'customers'";
        }

        protected String ddl() {
            return "${body[ddl]}";
        }

        protected String selectOp() {
            return "${body[op]} == 'r'";
        }

        protected String select() {
            return "insert into "
                + " ${body[source][table]} "
                + " (id, first_name, last_name, email) "
                + " values (${body[after][id]}, "
                        + " '${body[after][first_name]}', "
                        + " '${body[after][last_name]}', "
                        + " '${body[after][email]}') ";
        }

        protected String dmlOp() {
            return "${body[op]} == 'c' || ${body[op]} == 'u' || ${body[op]} == 'd'";
        }

        protected String dml() {
            // 获取sql需要设置变量
            // set binlog_rows_query_log_events=on;
            // c/u/d 可以解决where问题
            // 但是可能因客户端使用orm框架(或是用户编写)带有数据库的名称(例:database.table)
            return "${body[source][query]}";
        }
    }

    class GroovyRoute extends SimpleRoute {

        @Override
        public void configure() {
            from(uri())
                .routeId("kafka-demo")
                // .log("${body}")
                // .process(e -> {
                //         Message msg = e.getMessage();
                //         if (msg != null) {
                //             System.out.printf("--- message type: %s\n", msg.getClass().toString());
                //             Object body = msg.getBody();
                //             if (body != null) {
                //                 System.out.printf("--- body type: %s\n", body.getClass().toString());
                //             } else {
                //                 System.out.printf("--- body null\n");
                //             }
                //         } else {
                //             System.out.printf("message null\n");
                //         }
                //     })
                .unmarshal().json()
                // .convertBodyTo(LinkedHashMap.class)
                // .process(e -> {
                //         Message msg = e.getMessage();
                //         if (msg != null) {
                //             System.out.printf("--- message type: %s\n", msg.getClass().toString());
                //             Object body = msg.getBody();
                //             if (body != null) {
                //                 System.out.printf("--- body type: %s\n", body.getClass().toString());
                //             } else {
                //                 System.out.printf("--- body null\n");
                //             }
                //         } else {
                //             System.out.printf("message null\n");
                //         }
                //     })
                // .log("${body}")
                .choice()
                    .when().groovy(ddlOp())
                        .setBody().groovy(ddl())
                        .log("${body}")
                // .to("jdbc:demo")
                    .when().groovy(selectOp())
                        .setBody().groovy(select())
                        .log("${body}")
                // .to("jdbc:demo")
                    .when().groovy(dmlOp())
                        .setBody().groovy(dml())
                        .log("${body}")
                // .to("jdbc:demo")
                    .otherwise()
                        .log("discard --- ${body}")
                // .log("${body}")
                // .log("    on the topic ${headers[kafka.TOPIC]}")
                // .log("    on the partition ${headers[kafka.PARTITION]}")
                // .log("    with the offset ${headers[kafka.OFFSET]}")
                // .log("    with the key ${headers[kafka.KEY]}")
                ;
        }

        @Override
        protected String ddlOp() {
            return "def b = request.body;"
                + "b.ddl != null && b.source.table == 'customers'";
        }

        @Override
        protected String ddl() {
            return "def v = request.body.ddl;"
                + "def s = '`inventory`.';"
                + "if (v.contains(s)) {v = v.replace(s, ''); return v};"
                + "s = 'inventory.';"
                + "if (v.contains(s)) {v = v.replace(s, ''); return v};"
                + "v";
        }

        @Override
        protected String selectOp() {
            return "request.body.op == 'r'";
        }

        @Override
        protected String select() {
            return "def b = request.body;"
                + "\"\"\""
                + "insert into "
                + " ${b.source.table} "
                + " (id, first_name, last_name, email) "
                + " values (${b.after.id}, "
                        + " '${b.after.first_name}', "
                        + " '${b.after.last_name}', "
                        + " '${b.after.email}') "
                + "\"\"\"";
        }

        @Override
        protected String dmlOp() {
            return "def b = request.body;"
                + "b.op == 'c' || b.op == 'u' || b.op == 'd'";
        }

        @Override
        protected String dml() {
            // 获取sql需要设置变量
            // set binlog_rows_query_log_events=on;
            // c/u/d 可以解决where问题
            // 但是可能因客户端使用orm框架(或是用户编写)带有数据库的名称(例:database.table)
            return "def b = request.body;"
                + "if (b.source.query != null) { "
                    + "def q = b.source.query;"
                    + "def s = '`inventory`.';"
                    + "if (q.contains(s)) {q = q.replace(s, ''); return q};"
                    + "s = 'inventory.';"
                    + "if (q.contains(s)) {q = q.replace(s, ''); return q};"
                    + "q"
                + "} else if (b.op == 'c') {"
                    + "return \"\"\""
                            + "insert into ${b.source.table} "
                            + " (id, first_name, last_name, email) "
                            + " values (${b.after.id}, "
                                    + " '${b.after.first_name}', "
                                    + " '${b.after.last_name}', "
                                    + " '${b.after.email}') "
                            + "\"\"\""
                + "} else if (b.op == 'u') {"
                    + "return \"\"\""
                            + "update ${b.source.table} "
                            + " set "
                                + " id         = ${b.after.id}, "
                                + " first_name = '${b.after.first_name}', "
                                + " last_name  = '${b.after.last_name}', "
                                + " email      = '${b.after.email}'"
                            + " where "
                                + " id         = ${b.before.id} and "
                                + " first_name = '${b.before.first_name}' and "
                                + " last_name  = '${b.before.last_name}' and "
                                + " email      = '${b.before.email}'"
                            + "\"\"\""
                + "} else if (b.op == 'd') {"
                    + "return \"\"\""
                            + "delete from ${b.source.table} "
                            + " where "
                                + " id         = ${b.before.id} and "
                                + " first_name = '${b.before.first_name}' and "
                                + " last_name  = '${b.before.last_name}' and "
                                + " email      = '${b.before.email}'"
                            + "\"\"\""
                + "}";
        }
    }
}
