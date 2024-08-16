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

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@RequiredArgsConstructor
public class UserWriteDao implements UserEntityWriteRepository {

    private static final String USER_SAVE_FAILED_MESSAGE = "사용자 Sequence를 저장하지 못했습니다.";
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
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
                throw new DaoException(USER_SAVE_FAILED_MESSAGE, DATA_ACCESS_EXCEPTION);
            }
            return number.longValue();
        } catch (DataAccessException ex) {
            throw new DaoException(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }

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
}
