package project.dailyge.app.common.configuration.liquibase;

import javax.sql.DataSource;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquibaseConfig {

    @Bean
    public MultiTenantSpringLiquibase multiTenantSpringLiquibase(final DataSource dataSource) {
        final MultiTenantSpringLiquibase moduleConfig = new MultiTenantSpringLiquibase();
        moduleConfig.setDataSource(dataSource);
        moduleConfig.setChangeLog("/db/rdb/changelog/changelog-master.yaml");
        return moduleConfig;
    }
}
