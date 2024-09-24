package project.dailyge.app.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.validation.constraints.NotNull;
import static java.util.stream.Collectors.toSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationDataWriteDao {

    @Value("${profile.email}")
    private String email;

    private final EntityManager entityManager;
    private final Set<String> tableNames;
    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    public void initData() {
        save();
    }

    public void save() {
        try {
            for (int index = 1; index <= 10_000; index++) {
                insertCache(index);
            }
        } catch (Exception ex) {
            log.error("Cache initialized failed.");
        }
    }

    private void insertCache(int index) {
        final UserCache userCache = createUserCache(index);
        final byte[] compressedCache = compressAsByteArrayWithZstd(userCache, objectMapper);
        try {
            redisTemplate.opsForValue().set(getKey(userCache.getId()), compressedCache);
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private UserCache createUserCache(final int index) {
        return new UserCache((long) index, index + email, index + email, "", "NORMAL");
    }

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

    public void clearData() {
        this.tableNames.clear();
        final Set<String> tableNames = entityManager.getMetamodel()
            .getEntities().stream()
            .filter(isEntityType())
            .map(toLowerCase())
            .collect(toSet());
        this.tableNames.addAll(tableNames);
        try {
            for (final String table : this.tableNames) {
                final String query = String.format("TRUNCATE %s", table);
                jdbcTemplate.execute(query);
            }
        } catch (Exception ex) {
            log.error("RDB Data initialization failed: {}", ex.getMessage());
        }
    }

    @NotNull
    private Predicate<EntityType<?>> isEntityType() {
        return entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null;
    }

    @NotNull
    private Function<EntityType<?>, String> toLowerCase() {
        return entityType -> {
            final Table tableAnnotation = entityType.getJavaType().getAnnotation(Table.class);
            return tableAnnotation != null ? tableAnnotation.name().toLowerCase() : entityType.getName().toLowerCase();
        };
    }

    private String getKey(final Long userId) {
        return String.format("user:cache:%s", userId);
    }
}
