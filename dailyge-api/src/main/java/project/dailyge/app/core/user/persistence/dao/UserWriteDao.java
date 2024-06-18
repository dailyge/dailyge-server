package project.dailyge.app.core.user.persistence.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.domain.user.UserEntityWriteRepository;
import project.dailyge.domain.user.UserJpaEntity;

@Repository
@RequiredArgsConstructor
public class UserWriteDao implements UserEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public UserJpaEntity save(final UserJpaEntity user) {
        entityManager.persist(user);
        return user;
    }
}
