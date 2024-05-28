package project.dailyge.app.common.configuration;

import com.querydsl.jpa.impl.*;
import jakarta.persistence.*;
import org.springframework.context.annotation.*;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
