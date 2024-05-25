package project.dailyge.app.common;

import jakarta.annotation.*;
import jakarta.persistence.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import static org.testcontainers.shaded.com.google.common.base.CaseFormat.*;

import java.util.*;
import java.util.stream.*;

@Component
public class DatabaseInitializer {

    private static final String SET_FOREIGN_KEY_CHECKS_FALSE = "SET FOREIGN_KEY_CHECKS = 0";
    private static final String SET_FOREIGN_KEY_CHECKS_TRUE = "SET FOREIGN_KEY_CHECKS = 1";
    private final Set<String> tableNames = new HashSet<>();
    private final EntityManager entityManager;

    public DatabaseInitializer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        Set<String> tableNames = entityManager.getMetamodel()
            .getEntities().stream()
            .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
            .map(entityType -> UPPER_CAMEL.to(LOWER_UNDERSCORE, entityType.getName()))
            .collect(Collectors.toSet());

        this.tableNames.addAll(tableNames);
    }

    @Transactional
    public void truncateAllEntity() {
        entityManager.flush();
        entityManager.clear();

        entityManager.createNativeQuery(SET_FOREIGN_KEY_CHECKS_FALSE).executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        entityManager.createNativeQuery(SET_FOREIGN_KEY_CHECKS_TRUE).executeUpdate();
    }
}
