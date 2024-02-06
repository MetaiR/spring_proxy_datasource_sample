package com.github.metair.proxydatasource.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.MethodExecutionContext;
import net.ttddyy.dsproxy.listener.lifecycle.JdbcLifecycleEventListenerAdapter;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration(proxyBeanMethods = false)
@Slf4j
@RequiredArgsConstructor
public class DatasourceConfig {
    private static final ConcurrentHashMap<String, List<QueryInfo>> CONNECTION_QUERIES = new ConcurrentHashMap<>();

    @Bean
    @Primary
    DataSource proxyDatasource() {
        return ProxyDataSourceBuilder.create(dataSource())
                .retrieveIsolation()
                .writeIsolation()
                .afterQuery((execInfo, queryInfoList) -> {
                    var key = execInfo.getConnectionId();

                    var queries = CONNECTION_QUERIES.getOrDefault(key, new ArrayList<>());
                    queries.addAll(queryInfoList);

                    CONNECTION_QUERIES.put(execInfo.getConnectionId(), queries);
                })
                .listener(new JdbcLifecycleEventListenerAdapter() {
                    @Override
                    public void afterRollback(MethodExecutionContext executionContext) {
                        super.afterRollback(executionContext);
                        var key = executionContext.getConnectionInfo().getConnectionId();
                        CONNECTION_QUERIES.remove(key);
                        log.warn("removed all queries after rollback");
                    }

                    @Override
                    public void afterCommit(MethodExecutionContext executionContext) {
                        super.afterCommit(executionContext);
                        var key = executionContext.getConnectionInfo().getConnectionId();
                        try {
                            var queries = CONNECTION_QUERIES.getOrDefault(key, List.of());

                            queries.forEach(q -> {
                                log.info("query: " + q.getQuery());
                                var params = q.getParametersList().stream().flatMap(Collection::stream).map(ParameterSetOperation::getArgs).map(objects -> objects[1]).toList();

                                log.info("params: " + params);
                            });
                        } finally {
                            CONNECTION_QUERIES.remove(key);
                        }
                    }
                })
                .build();
    }

    private DataSource dataSource() {
        var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/mydatabase");
        dataSourceBuilder.username("myuser");
        dataSourceBuilder.password("secret");
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        return dataSourceBuilder.build();
    }
}
