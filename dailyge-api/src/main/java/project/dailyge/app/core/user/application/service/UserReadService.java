package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.dto.response.UserInfoResponse;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserJpaEntity;
import project.dailyge.domain.user.UserJpaRepository;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
class UserReadService implements UserReadUseCase {

    private final UserJpaRepository userRepository;

    @Override
    public UserJpaEntity findById(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> UserTypeException.from(USER_NOT_FOUND));
    }

    @Override
    public UserInfoResponse findUserInfoById(final Long userId) {
        final UserJpaEntity findUser = userRepository.findByIdAndDeleted(userId, false);
        if (findUser == null) {
            throw UserTypeException.from(USER_NOT_FOUND);
        }
        return new UserInfoResponse(findUser);
    }
}
