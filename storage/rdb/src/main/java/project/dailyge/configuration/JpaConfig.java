package project.dailyge.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = {"project.dailyge.entity"})
@EnableJpaRepositories(basePackages = {"project.dailyge.entity"})
public class JpaConfig {
}
