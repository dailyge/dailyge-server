package project.dailyge.entity.user;

public interface UserEntityWriteRepository {
    Long save(String email);

    UserJpaEntity save(UserJpaEntity user);

    void delete(UserJpaEntity user);
}
