package project.dailyge.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = {"project.dailyge.entity"})
@EnableJpaRepositories(basePackages = {"project.dailyge.entity"})
public class DailygeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailygeApplication.class);
    }
}
