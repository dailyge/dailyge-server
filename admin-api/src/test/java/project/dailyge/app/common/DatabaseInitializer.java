package project.dailyge.app.common;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.ClassUtils.resolveClassName;
import static org.testcontainers.shaded.com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static org.testcontainers.shaded.com.google.common.base.CaseFormat.UPPER_CAMEL;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer {

    private static final String SET_FOREIGN_KEY_CHECKS_FALSE = "SET FOREIGN_KEY_CHECKS = 0";
    private static final String SET_FOREIGN_KEY_CHECKS_TRUE = "SET FOREIGN_KEY_CHECKS = 1";

    private final Set<String> tableNames;
    private static final Set<String> excludeTables = Set.of("databasechangelog", "databasechangeloglock");
    private final Set<String> collectionNames;
    private final EntityManager entityManager;
    private final MongoTemplate mongoTemplate;

    public DatabaseInitializer(
        final EntityManager entityManager,
        final MongoTemplate mongoTemplate
    ) {
        this.entityManager = entityManager;
        this.mongoTemplate = mongoTemplate;
        this.tableNames = new HashSet<>();
        this.collectionNames = new HashSet<>();
    }

    @PostConstruct
    public void afterPropertiesSet() {
        initRdb();
        initDocument();
    }

    private void initRdb() {
        final Set<String> tableNames = entityManager.getMetamodel()
            .getEntities().stream()
            .filter(isEntityType())
            .map(toLowerCase())
            .collect(Collectors.toSet());
        this.tableNames.addAll(tableNames);
        this.tableNames.removeAll(excludeTables);
    }

    @NotNull
    private Predicate<EntityType<?>> isEntityType() {
        return entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null;
    }

    @NotNull
    private Function<EntityType<?>, String> toLowerCase() {
        return entityType -> UPPER_CAMEL.to(LOWER_UNDERSCORE, entityType.getName());
    }

    private void initDocument() {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));

        final Set<Class<?>> documentClazzes = scanner.findCandidateComponents("project.dailyge")
            .stream()
            .map(getBeanDefinitionClassFunction())
            .collect(Collectors.toSet());

        for (final Class<?> documentClazz : documentClazzes) {
            final String collectionName = documentClazz.getAnnotation(Document.class)
                .collection();
            if (!collectionName.isBlank()) {
                this.collectionNames.add(collectionName);
                continue;
            }

            final String documentToLowerCase = documentClazz.getSimpleName().toLowerCase();
            this.collectionNames.add(documentToLowerCase);
        }
    }

    @NotNull
    private Function<BeanDefinition, ? extends Class<?>> getBeanDefinitionClassFunction() {
        return beanDefinition -> resolveClassName(
            Objects.requireNonNull(beanDefinition.getBeanClassName()),
            this.getClass().getClassLoader()
        );
    }

    @Transactional
    public void initData() {
        initRdbData();
        initDocumentDbData();
    }

    private void initRdbData() {
        entityManager.flush();
        entityManager.clear();

        entityManager.createNativeQuery(SET_FOREIGN_KEY_CHECKS_FALSE).executeUpdate();
        for (final String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        entityManager.createNativeQuery(SET_FOREIGN_KEY_CHECKS_TRUE).executeUpdate();
    }

    private void initDocumentDbData() {
        for (final String collectionName : this.collectionNames) {
            mongoTemplate.remove(new Query(), collectionName);
        }
    }
}
