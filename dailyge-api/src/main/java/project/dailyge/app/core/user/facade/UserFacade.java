package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.common.EventType.UPDATE;
import static project.dailyge.entity.user.Role.NORMAL;
import static project.dailyge.entity.user.UserEvent.createEvent;

@RequiredArgsConstructor
@FacadeLayer(value = "UserFacade")
public class UserFacade {

    private static final String FIXED_IMAGE_URL = "https://shorturl.at/dejs2";

    private final GoogleOAuthManager googleOAuthManager;
    private final UserReadUseCase userReadUseCase;
    private final UserWriteUseCase userWriteUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final EventPublisher<UserEvent> userEventPublisher;
    private final EventPublisher<TaskEvent> taskEventEventPublisher;
    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserCacheWriteUseCase userCacheWriteUseCase;

    public DailygeToken login(final String code) {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Long findUserId = userReadUseCase.findUserIdByEmail(response.getEmail());
        final Long userId = publishEvent(findUserId, response);
        final DailygeToken token = tokenProvider.createToken(userId);
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long publishEvent(
        final Long userId,
        final GoogleUserInfoResponse response
    ) {
        if (userId == null) {
            final Long newUserId = userWriteUseCase.save(response.getEmail());
            final TaskEvent taskEvent = TaskEvent.createEvent(newUserId, createTimeBasedUUID(), CREATE);
            final UserEvent userEvent = createEvent(newUserId, createTimeBasedUUID(), CREATE);
            executeEvent(userEvent, taskEvent);
            return newUserId;
        }
        final UserEvent userEvent = createEvent(userId, createTimeBasedUUID(), UPDATE);
        userEventPublisher.publishInternalEvent(userEvent);
        return userId;
    }

    private void executeEvent(
        final UserEvent userEvent,
        final TaskEvent taskEvent
    ) {
        userEventPublisher.publishInternalEvent(userEvent);
        taskEventEventPublisher.publishInternalEvent(taskEvent);
    }

    public void logout(final Long userId) {
        tokenManager.deleteRefreshToken(userId);
    }

    public void delete(final Long userId) {
        userWriteUseCase.delete(userId);
        userCacheWriteUseCase.delete(userId);
        logout(userId);
    }

    public void saveCache(final UserEvent event) {
        if (CREATE.equals(event.getEventType())) {
            final String findEmail = userReadUseCase.findEmailByUserId(event.getPublisher());
            final UserCache userCache = new UserCache(
                event.getPublisher(), findEmail, findEmail, FIXED_IMAGE_URL, NORMAL.name()
            );
            userCacheWriteUseCase.save(userCache);
        }
    }

    public void updateCache(
        final Long userId,
        final String nickname
    ) {
        final UserCache userCache = userCacheReadUseCase.findById(userId);
        final UserCache updateUserCache = new UserCache(
            userId,
            nickname != null ? nickname : userCache.getNickname(),
            userCache.getEmail(),
            userCache.getProfileImageUrl(),
            userCache.getRole()
        );
        userCacheWriteUseCase.save(updateUserCache);
    }

    public void updateUser(
        final Long userId,
        final UserUpdateCommand command
    ) {
        updateCache(userId, command.nickname());
    }
}
