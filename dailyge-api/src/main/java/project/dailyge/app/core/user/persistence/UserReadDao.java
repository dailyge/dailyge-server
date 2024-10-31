package project.dailyge.app.core.user.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.entity.user.QUserJpaEntity.userJpaEntity;
import project.dailyge.entity.user.UserEntityReadRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@Repository
public class UserReadDao implements UserEntityReadRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    public UserReadDao(
        final JdbcTemplate jdbcTemplate,
        final JPAQueryFactory queryFactory
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<UserJpaEntity> findById(final Long userId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(userJpaEntity)
            .where(userJpaEntity.id.eq(userId))
            .fetchOne());
    }

    @Override
    public Optional<UserJpaEntity> findActiveUserById(final Long userId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(userJpaEntity)
            .where(
                userJpaEntity.id.eq(userId),
                userJpaEntity._deleted.isFalse()
            )
            .fetchOne());
    }

    @Override
    public Optional<UserJpaEntity> findActiveUserByEmail(final String email) {
        return Optional.ofNullable(queryFactory
            .selectFrom(userJpaEntity)
            .where(
                userJpaEntity.email.eq(email),
                userJpaEntity._deleted.isFalse()
            )
            .fetchOne());
    }

    @Override
    public Optional<UserJpaEntity> findByNickname(final String nickname) {
        return Optional.ofNullable(
            queryFactory.selectFrom(userJpaEntity)
                .where(
                    userJpaEntity.nickname.eq(nickname)
                        .and(userJpaEntity._deleted.eq(false))
                )
                .fetchOne()
        );
    }

    @Override
    public Long findUserIdByEmail(final String email) {
        final String sql = "SELECT id FROM user_sequence WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, email);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public String findEmailById(final Long userId) {
        final String sql = "SELECT email FROM user_sequence WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (DataAccessException ex) {
            return null;
        }
    }
}
