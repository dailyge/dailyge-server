package project.dailyge.lock.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final String DELIMITER = ":";
    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.connectionPoolSize}")
    private int connectionPoolSize;

    @Value("${spring.data.redis.minConnectionDleSize}")
    private int minConnectionDleSize;

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer()
            .setAddress(REDISSON_HOST_PREFIX + redisHost + DELIMITER + redisPort)
            .setConnectionPoolSize(connectionPoolSize)
            .setConnectionMinimumIdleSize(minConnectionDleSize);
        return Redisson.create(config);
    }
}
