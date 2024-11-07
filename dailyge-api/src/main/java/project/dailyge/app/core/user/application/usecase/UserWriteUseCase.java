package project.dailyge.app.core.user.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.DUPLICATED_EMAIL;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.entity.user.UserEntityReadRepository;
import project.dailyge.entity.user.UserEntityWriteRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@ApplicationLayer(value = "UserWriteService")
public class UserWriteUseCase implements UserWriteService {

    private final UserEntityReadRepository userReadRepository;
    private final UserEntityWriteRepository userWriteRepository;

    public UserWriteUseCase(
        final UserEntityReadRepository userReadRepository,
        final UserEntityWriteRepository userWriteRepository
    ) {
        this.userReadRepository = userReadRepository;
        this.userWriteRepository = userWriteRepository;
    }

    @Override
    @Transactional
    public Long save(
        final String email,
        final String nickname
    ) {
        return userWriteRepository.save(email, nickname);
    }

    @Override
    @Transactional
    public UserJpaEntity save(final UserJpaEntity user) {
        final Optional<UserJpaEntity> findUser = userReadRepository.findActiveUserByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw UserTypeException.from(DUPLICATED_EMAIL);
        }
        return userWriteRepository.save(user);
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final UserUpdateCommand command
    ) {
        final UserJpaEntity findUser = userReadRepository.findActiveUserById(dailygeUser.getUserId())
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
        findUser.updateNickname(command.nickname());
    }

    @Override
    @Transactional
    public UserJpaEntity upsert(final UserJpaEntity newUser) {
        final Optional<UserJpaEntity> findUser = userReadRepository.findActiveUserByEmail(newUser.getEmail());
        if (findUser.isEmpty()) {
            return userWriteRepository.save(newUser);
        }
        final UserJpaEntity user = findUser.get();
        if (newUser.getProfileImageUrl() != null) {
            user.profileImageInit(newUser.getProfileImageUrl());
        }
        return userWriteRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(final Long userId) {
        final UserJpaEntity findUser = userReadRepository.findActiveUserById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
        findUser.delete();
        userWriteRepository.save(findUser);
    }
}
