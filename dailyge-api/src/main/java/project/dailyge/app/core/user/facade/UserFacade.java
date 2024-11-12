package project.dailyge.app.core.user.facade;

import java.util.Optional;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.auth.DailygeToken;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.core.cache.user.UserCacheWriteService;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_SERVICE_UNAVAILABLE;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.common.EventType.UPDATE;
import static project.dailyge.entity.user.UserEvent.createEvent;

@FacadeLayer(value = "UserFacade")
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final EventPublisher<UserEvent> userEventPublisher;
    private final EventPublisher<TaskEvent> taskEventEventPublisher;
    private final UserCacheReadService userCacheReadService;
    private final UserCacheWriteService userCacheWriteService;

    public UserFacade(
        final GoogleOAuthManager googleOAuthManager,
        final UserReadService userReadService,
        final UserWriteService userWriteService,
        final TokenProvider tokenProvider,
        final TokenManager tokenManager,
        final EventPublisher<UserEvent> userEventPublisher,
        final EventPublisher<TaskEvent> taskEventEventPublisher,
        final UserCacheReadService userCacheReadService,
        final UserCacheWriteService userCacheWriteService
    ) {
        this.googleOAuthManager = googleOAuthManager;
        this.userReadService = userReadService;
        this.userWriteService = userWriteService;
        this.tokenProvider = tokenProvider;
        this.tokenManager = tokenManager;
        this.userEventPublisher = userEventPublisher;
        this.taskEventEventPublisher = taskEventEventPublisher;
        this.userCacheReadService = userCacheReadService;
        this.userCacheWriteService = userCacheWriteService;
    }

    public DailygeToken login(final String code) {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Optional<UserJpaEntity> findUser = userReadService.findActiveUserByEmail(response.getEmail());
        final Long userId = publishEvent(findUser, response);
        final DailygeToken token = tokenProvider.createToken(userId);
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long publishEvent(
        final Optional<UserJpaEntity> user,
        final GoogleUserInfoResponse response
    ) {
        if (user.isPresent()) {
            final UserJpaEntity existingUser = user.get();
            if (existingUser.isBlacklist()) {
                throw UserTypeException.from(USER_SERVICE_UNAVAILABLE);
            }
            final UserEvent userEvent = createEvent(existingUser.getId(), createTimeBasedUUID(), UPDATE);
            userEventPublisher.publishInternalEvent(userEvent);
            return existingUser.getId();
        }
        final Long newUserId = userWriteService.save(response.getEmail(), response.getName());
        final TaskEvent taskEvent = TaskEvent.createEvent(newUserId, createTimeBasedUUID(), CREATE);
        final UserEvent userEvent = createEvent(newUserId, createTimeBasedUUID(), CREATE);
        executeEvent(userEvent, taskEvent);
        return newUserId;
    }

    private void executeEvent(
        final UserEvent userEvent,
        final TaskEvent taskEvent
    ) {
        userEventPublisher.publishInternalEvent(userEvent);
        taskEventEventPublisher.publishInternalEvent(taskEvent);
    }

    public void logout(final Long userId) {
        userCacheWriteService.delete(userId);
        tokenManager.deleteRefreshToken(userId);
    }

    public void delete(final Long userId) {
        userWriteService.delete(userId);
        logout(userId);
    }

    public void saveCache(final UserEvent event) {
        if (CREATE.equals(event.getEventType())) {
            saveCache(event.getPublisher());
        }
        if (UPDATE.equals(event.getEventType())) {
            if (!userCacheReadService.existsById(event.getPublisher())) {
                saveCache(event.getPublisher());
            }
        }
    }

    private void saveCache(final Long userId) {
        final UserJpaEntity findUser = userReadService.findActiveUserById(userId);
        final UserCache userCache = new UserCache(
            userId, findUser.getNickname(), findUser.getEmail(), findUser.getProfileImageUrl(), findUser.getRoleAsString()
        );
        userCacheWriteService.save(userCache);
    }

    public void update(
        final DailygeUser dailygeUser,
        final UserUpdateCommand command
    ) {
        userWriteService.update(dailygeUser, command);
        final UserCache userCache = userCacheReadService.findById(dailygeUser.getUserId());
        final UserCache updateUserCache = new UserCache(
            dailygeUser.getUserId(),
            command.nickname() != null ? command.nickname() : userCache.getNickname(),
            userCache.getEmail(),
            userCache.getProfileImageUrl(),
            userCache.getRole()
        );
        userCacheWriteService.save(updateUserCache);
    }
}
