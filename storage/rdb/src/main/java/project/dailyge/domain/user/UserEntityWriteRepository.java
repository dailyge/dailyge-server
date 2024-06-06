package project.dailyge.domain.user;

public interface UserEntityWriteRepository {
    UserJpaEntity save(UserJpaEntity user);

    void delete(UserJpaEntity user);
}
