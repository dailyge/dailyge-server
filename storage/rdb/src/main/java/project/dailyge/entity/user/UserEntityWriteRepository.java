package project.dailyge.entity.user;

public interface UserEntityWriteRepository {
    UserJpaEntity save(UserJpaEntity user);

    void delete(UserJpaEntity user);
}
