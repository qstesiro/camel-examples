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
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Message;

import org.apache.commons.dbcp2.*;
import javax.sql.DataSource;

/*
  docker run \
      --name mysql \
      --network dbz \
      --network-alias mysql \
      -p 3306:3306 \
      -e MYSQL_ROOT_PASSWORD=debezium \
      -e MYSQL_USER=mysqluser \
      -e MYSQL_PASSWORD=mysqluser \
      --rm -d \
      debezium/example-mysql:1.7

   # 客户端
   docker run \
       --net host \
       -it --rm \
       debezium/example-mysql:1.7 \
       mysql -h127.0.0.1 \
       -P3306 \
       -umysqluser \
       -pmysqluser \
       -Dinventory \
       --prompt 'mysqluser> '
 */

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class JdbcDemo {

    // 测试环境
    private final static String USER = "console";
    private final static String PASSWORD = "suMuCaSu1e";

    private final static String URI = "jdbc:mysql://10.138.228.243:3306/console?characterEncoding=utf8";

    public void route() throws Exception {
        try (CamelContext camel = new DefaultCamelContext(getRegistry())) {
            camel.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() {
                        from("timer://demo?period=1000&repeatCount=1")
                            .routeId("jdbc-demo")
                            .setBody(constant("select cluster, namespace from app_record"))
                            .to("jdbc:demo")
                            .split().jsonpath("$.[*]")
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
                            // .log("${body[\"cluster\"]}");
                            .setBody(simple("insert into app_record_test(cluster, namespace) values('${body[\"cluster\"]}', '${body[\"namespace\"]}')"))
                            .to("jdbc:demo");
                    }
                });
            camel.start();
            Thread.sleep(10_100_000);
            camel.stop();
        }
    }

    private DefaultRegistry getRegistry() {
        DefaultRegistry reg = new DefaultRegistry();
        reg.bind("demo", getDataSource());
        return reg;
    }

    private DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        ds.setUrl(URI);
        return ds;
    }
}
