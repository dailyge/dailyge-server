package project.dailyge.app;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.data.jpa.repository.config.*;

@SpringBootApplication
@EntityScan(basePackages = {"project.dailyge.domain"})
@EnableJpaRepositories(basePackages = {"project.dailyge.domain"})
public class DailygeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailygeApplication.class);
    }
}
