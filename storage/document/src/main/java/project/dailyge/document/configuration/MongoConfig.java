package project.dailyge.document.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(
        final MongoDatabaseFactory factory,
        final MongoMappingContext context
    ) {
        return new MappingMongoConverter(new DefaultDbRefResolver(factory), context);
    }

    @Bean
    public IsNewAwareAuditingHandler isNewAwareAuditingHandler(MongoMappingContext context) {
        return new IsNewAwareAuditingHandler(PersistentEntities.of(context));
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(), "dailyge");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory, MappingMongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }
}
