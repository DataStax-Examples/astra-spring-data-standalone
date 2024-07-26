package com.example;

import com.datastax.oss.driver.api.core.CqlSession;
import com.example.conf.AstraDBConfiguration;
import com.example.todo.Todo;
import com.example.todo.TodoRepositoryCassandra;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Unit test for simple App.
 */

@Slf4j
@SpringJUnitConfig({AstraDBConfiguration.class, TodoRepositoryCassandra.class})
public class SpringDataAstraIntegrationTest {

    @Autowired
    CqlSession cqlSession;

    @Autowired
    TodoRepositoryCassandra todoRepositoryCassandra;

    @Test
    public void shouldConnectAndCreateTables() {
        String cassandraVersion = cqlSession
                .execute("SELECT release_version FROM system.local")
                .one()
                .getString("release_version");
        log.info("Using the CQLSESSION {}", cqlSession);
    }

    @Test
    public void shouldUseCassandraRepository() {
        cqlSession.execute("CREATE TABLE IF NOT EXISTS todos (" +
                "uid UUID PRIMARY KEY, " +
                "title TEXT, " +
                "completed BOOLEAN, " +
                "offset INT)");
        log.info("Table Created");

        Todo task1 = new Todo("Learn Spring", 1);
        Todo task2 = new Todo("Learn Cassandra", 2);
        todoRepositoryCassandra.saveAll(List.of(task1, task2));
        log.info("Entities inserted");

        todoRepositoryCassandra.findAll().iterator().forEachRemaining(todo -> {
            log.info("Found Todo: {} : {} ", todo.getUid(), todo.getTitle());
        });
    }

}
