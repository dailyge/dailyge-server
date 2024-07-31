package project.dailyge.app.core.user.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import project.dailyge.app.common.exception.DaoException;
import project.dailyge.entity.user.UserEntityWriteRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserWriteDao implements UserEntityWriteRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserJpaEntity save(final UserJpaEntity user) {
        entityManager.persist(user);
        return user;
    }

    /**
     * 운영을 위해 만든 메서드로, 외부에서 호출하지 말 것.
     */
    @Override
    @Transactional
    public void delete(UserJpaEntity user) {
        entityManager.detach(user);
    }

    @Override
    public Long getSequence() {
        final String sql = "INSERT INTO user_sequence (id, executed) VALUES (null, 0)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> connection.prepareStatement(sql, new String[]{"id"}), keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (DataAccessException ex) {
            throw new DaoException(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }
}
