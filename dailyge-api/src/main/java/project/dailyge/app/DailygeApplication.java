package project.dailyge.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"project.dailyge.domain"})
@EnableJpaRepositories(basePackages = {"project.dailyge.domain"})
public class DailygeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailygeApplication.class);
    }
}
