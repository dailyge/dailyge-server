package project.dailyge.document.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import static com.mongodb.MongoCredential.createCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.AuditingEntityCallback;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"project.dailyge"})
public class MongoConfig {

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.credential}")
    private String credentialUser;

    @Value("${spring.data.mongodb.schema}")
    private String schema;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.connection.max-connection}")
    private int maxConnection;

    @Value("${spring.data.mongodb.connection.min-connection}")
    private int minConnection;

    @Value("${spring.data.mongodb.connection.max-wait-time}")
    private long maxWaitTime;

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Collections.emptyList());
    }

    @Bean
    public AuditingEntityCallback auditingEntityCallback(final ObjectFactory<IsNewAwareAuditingHandler> handler) {
        return new AuditingEntityCallback(handler);
    }

    @Bean
    public MongoClient mongoClient() {
        final MongoCredential credential = createCredential(username, credentialUser, password.toCharArray());
        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyToConnectionPoolSettings(builder ->
                builder.maxSize(maxConnection)
                    .minSize(minConnection)
                    .maxWaitTime(maxWaitTime, MILLISECONDS)
                    .maxConnectionLifeTime(0, MILLISECONDS)
                    .maxConnectionIdleTime(0, MILLISECONDS)
                    .maintenanceInitialDelay(0, MILLISECONDS)
                    .maintenanceFrequency(60_000, MILLISECONDS))
            .applyToClusterSettings(builder ->
                builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
            .applyToClusterSettings(builder ->
                builder.serverSelectionTimeout(30, SECONDS))
            .applyToSocketSettings(builder ->
                builder.connectTimeout(30, SECONDS).readTimeout(30, SECONDS)
            )
            .credential(credential)
            .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), schema);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(
        final MongoDatabaseFactory mongoDatabaseFactory,
        final MongoMappingContext mongoMappingContext
    ) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(
        final MongoDatabaseFactory factory,
        final MappingMongoConverter converter
    ) {
        return new MongoTemplate(factory, converter);
    }
}
