package project.dailyge.entity.user;

public interface UserEntityWriteRepository {
    Long save(String email, String nickname);

    UserJpaEntity save(UserJpaEntity user);

    void delete(UserJpaEntity user);
}
