package project.dailyge.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        if (password != null && !password.isBlank()) {
            config.setPassword(password);
        }
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ByteArrayRedisSerializer());
        return template;
    }

    public static class ByteArrayRedisSerializer implements RedisSerializer<byte[]> {
        @Override
        public byte[] serialize(byte[] byteArray) throws SerializationException {
            return byteArray;
        }

        @Override
        public byte[] deserialize(byte[] byteArray) throws SerializationException {
            return byteArray;
        }
    }
}
