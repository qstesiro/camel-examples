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

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class BeanDemo {

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
                            // 尽量显示指定方法,如果不指定camel自动选择算法比较复杂
                            .bean(new Bean(), "enhance")
                            .bean("bean", "enhance")
                            .bean(new Bean(), "print")
                            .bean("bean", "print")
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
        reg.bind("bean", new Bean());
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

    private static class Bean {

        private static int index = 0;

        public Map<String, String> enhance(Map<String, String> map) {
            index += 1;
            map.put("cluster", String.format("%s-%d", map.get("cluster"), index));
            map.put("namespace", String.format("%s-%d", map.get("namespace"), index));
            return map;
        }

        public void print(Map<String, String> map) {
            System.out.println(map);
        }
    }
}
