package project.dailyge.app.core.user.persistence.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.domain.user.UserEntityWriteRepository;
import project.dailyge.domain.user.UserJpaEntity;

@Repository
@RequiredArgsConstructor
public class UserWriteDao implements UserEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserJpaEntity save(UserJpaEntity user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(UserJpaEntity user) {
        entityManager.remove(user);
    }
}
