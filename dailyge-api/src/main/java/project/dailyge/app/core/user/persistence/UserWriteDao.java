package project.dailyge.app.core.user.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.user.Role;
import project.dailyge.entity.user.UserEntityWriteRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserWriteDao implements UserEntityWriteRepository {

    private final EntityManager entityManager;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserWriteDao(
        final EntityManager entityManager,
        final NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.entityManager = entityManager;
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Long save(
        final String email,
        final String nickname
    ) {
        final UserJpaEntity newUser = new UserJpaEntity(null, nickname, email);
        entityManager.persist(newUser);
        return newUser.getId();
    }

    @Override
    public UserJpaEntity save(final UserJpaEntity user) {
        entityManager.persist(user);
        return user;
    }

    public UserJpaEntity insertUser(
        final Long userId,
        final String email,
        final String nickname
    ) {
        final String sql = "INSERT INTO users (id, nickname, email, created_at, deleted, user_role, profile_image_url, is_blacklist ) "
            + "VALUES (:id, :nickname, :email, :created_at, :deleted, :user_role, :profile_image_url, :is_blacklist)";
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", userId);
        parameters.put("nickname", nickname);
        parameters.put("email", email);
        parameters.put("created_at", new Timestamp(System.currentTimeMillis()));
        parameters.put("deleted", 0);
        parameters.put("user_role", "NORMAL");
        parameters.put("profile_image_url", "");
        parameters.put("is_blacklist", false);
        jdbcTemplate.update(sql, new MapSqlParameterSource(parameters));
        return new UserJpaEntity(userId, nickname, email, Role.NORMAL);
    }

    /**
     * 운영을 위해 만든 메서드로, 외부에서 호출하지 말 것.
     */
    @Override
    @Transactional
    public void delete(final UserJpaEntity user) {
        entityManager.detach(user);
    }
}
