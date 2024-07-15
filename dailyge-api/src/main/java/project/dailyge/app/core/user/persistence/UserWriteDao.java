package project.dailyge.app.core.user.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.user.UserEntityWriteRepository;
import project.dailyge.entity.user.UserJpaEntity;

@Repository
@RequiredArgsConstructor
public class UserWriteDao implements UserEntityWriteRepository {

    private final EntityManager entityManager;

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
