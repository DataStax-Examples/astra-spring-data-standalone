package com.example.conf;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * Hello world!
 *
 */
@Slf4j
@Configuration
@EnableCassandraRepositories(basePackages = "com.example.todo")
public class AstraDBConfiguration {

    @Bean
    public CqlSession cqlSessionAstraDB() {
        // If information in application.conf
        CqlSession session =  new CqlSessionBuilder().build();

        /*
        CqlSession session =  new CqlSessionBuilder()
                .withAuthCredentials("token", "AstraCS:aaa")
                .withKeyspace("default_keyspace")
                .withCloudSecureConnectBundle(Paths.get("tmp/secure-connect-bundle-for-your-database.zip"))
                .build();
        */

        log.info("CqlSession initialized against keyspace {}", session.getKeyspace());
        return session;
    }

    @Bean
    public CassandraTemplate cassandraTemplate() {
        CassandraTemplate template = new CassandraTemplate(cqlSessionAstraDB());
        log.info("CassandraTemplate initialized with operations {}", template.getCqlOperations().describeRing());
        return template;
    }

}
