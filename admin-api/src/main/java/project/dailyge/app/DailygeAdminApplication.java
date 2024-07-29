package project.dailyge.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import project.dailyge.configuration.JpaConfig;
import project.dailyge.configuration.QueryDslConfig;
import project.dailyge.configuration.RedisConfig;
import project.dailyge.document.configuration.MongoConfig;
import project.dailyge.lock.configuration.RedissonConfig;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
@Import(value = {JpaConfig.class, MongoConfig.class, RedisConfig.class, RedissonConfig.class, QueryDslConfig.class})
public class DailygeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailygeAdminApplication.class);
    }
}
