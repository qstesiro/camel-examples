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

import org.apache.camel.component.elasticsearch.ElasticsearchComponent;

/**
 * A simple example to consume data from Debezium and send it to Kinesis
 */
public final class EsDemo {

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

        public final static String ES_QUERY = "{\"query\": {\"bool\": {\"must\": {\"term\": {\"idsite\": \"btbrrs\"}},\"filter\": {\"range\": {\"visitTimestamp\": {\"gte\": \"now-1d/d\",\"lt\": \"now/d\"}}}}},\"size\": 1}";
        // \"aggs\": {\"idvisitor\": {\"cardinality\": {\"field\": \"idvisitor\"}}}

        @Override
        public void configure() {
            getContext().setAutowiredEnabled(false);
            ElasticsearchComponent com = new ElasticsearchComponent();
            com.setHostAddresses("10.138.16.190:9200");
            com.setUser("elastic");
            com.setPassword("f8a50d6c");
            com.setEnableSSL(false);
            getContext().addComponent("es-rest", com);
            from(uri())
                .routeId("es-demo")
                .setBody(constant(ES_QUERY))
                .to("es-rest:cluster?indexName=matomo_visit_log_202204&operation=Search")
                // .unmarshal().json()
                .log("${body}")
                ;
        }

        protected String uri() {
            return "timer:demo"
                + "?delay=0"
                + "&period=60000"
                + "&repeatCount=1000";
        }
    }
}
