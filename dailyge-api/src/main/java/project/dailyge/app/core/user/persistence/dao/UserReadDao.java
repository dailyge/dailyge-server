package project.dailyge.app.core.user.persistence.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.user.UserEntityReadRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.entity.user.QUserJpaEntity.userJpaEntity;

@Repository
@RequiredArgsConstructor
public class UserReadDao implements UserEntityReadRepository {

    private final JPAQueryFactory queryFactory;

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
                userJpaEntity.deleted.isFalse()
            )
            .fetchOne());
    }

    @Override
    public Optional<UserJpaEntity> findActiveUserByEmail(final String email) {
        return Optional.ofNullable(queryFactory
            .selectFrom(userJpaEntity)
            .where(
                userJpaEntity.email.eq(email),
                userJpaEntity.deleted.isFalse()
            )
            .fetchOne());
    }
}
