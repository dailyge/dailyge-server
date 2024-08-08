package project.dailyge.app.common.data;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.user.UserJpaEntity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
// @Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class OperationDataInitializer implements CommandLineRunner {

    private static final String LOCAL = "local";

    @Value("${env}")
    private String env;

    @Value("${profile.nickname}")
    private String nickname;

    @Value("${profile.email}")
    private String email;

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final Set<String> collectionNames = new HashSet<>();
    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void run(final String... args) {
        log.info(env);
        if (isEnv(LOCAL)) {
            initSchema();
            initCollection();
            initData();
        }
    }

    public void initSchema() {
        if (isEnv(LOCAL)) {
            final Path rootPath = Paths.get("").toAbsolutePath();
            final Path sqlFilePath = rootPath.resolve("config/schema/schema.sql");
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new FileSystemResource(sqlFilePath));
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to execute SQL script", ex);
            }
        }
    }

    private void initCollection() {
        final MongoDatabase database = mongoTemplate.getDb();
        final MongoIterable<String> collectionNames = database.listCollectionNames();
        for (final String collectionName : collectionNames) {
            this.collectionNames.add(collectionName);
        }
    }

    public void initData() {
        if (isEnv(LOCAL)) {
            try {
                entityManager.persist(new UserJpaEntity(nickname, email));
            } catch (Exception ex) {
                log.error("Data initialization failed: {}", ex.getMessage());
            }
        }
    }

    @PreDestroy
    @Transactional
    public void clearData() {
        if (isEnv(LOCAL)) {
            jdbcTemplate.execute("TRUNCATE users");
            for (final String collectionName : this.collectionNames) {
                mongoTemplate.remove(new Query(), collectionName);
            }
        }
    }

    private boolean isEnv(final String env) {
        return LOCAL.equals(env);
    }
}
