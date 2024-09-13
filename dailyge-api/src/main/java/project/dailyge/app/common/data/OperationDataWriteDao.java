package project.dailyge.app.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import static java.time.Duration.ofDays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import project.dailyge.core.cache.user.UserCache;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationDataWriteDao {
    private static final long CACHE_DURATION = 90;

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    public Long save(final String email) {
        final String sql = "INSERT INTO user_sequence (email, executed) VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                final PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, email);
                ps.setBoolean(2, false);
                return ps;
            }, keyHolder);

            final Number number = keyHolder.getKey();
            if (number == null) {
                throw CommonException.from("Data inserts failure.", DATA_ACCESS_EXCEPTION);
            }
            return number.longValue();
        } catch (DataAccessException ex) {
            log.error("User data initialized failed.");
            throw CommonException.from(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }

    public void save(final UserCache userCache) {
        final byte[] compressedCache = compressAsByteArrayWithZstd(userCache, objectMapper);
        try {
            executeRedisCommand(() ->
                redisTemplate.opsForValue().set(
                    getKey(userCache.getId()),
                    compressedCache,
                    ofDays(CACHE_DURATION)
                )
            );
        } catch (Exception ex) {
            log.error("Cache initialized failed.");
        }
    }

    private void executeRedisCommand(final Runnable command) {
        try {
            command.run();
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private String getKey(final Long userId) {
        return String.format("user:cache:%s", userId);
    }
}
