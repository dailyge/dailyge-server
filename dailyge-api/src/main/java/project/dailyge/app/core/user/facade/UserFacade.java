package project.dailyge.app.core.user.facade;

import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.auth.DailygeToken;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.core.cache.user.UserCacheWriteService;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.common.EventType.UPDATE;
import project.dailyge.entity.task.TaskEvent;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEvent;

@FacadeLayer(value = "UserFacade")
public class UserFacade {

    private static final String FIXED_IMAGE_URL = "https://shorturl.at/dejs2";

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
        final Long findUserId = userReadService.findUserIdByEmail(response.getEmail());
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
            final Long newUserId = userWriteService.save(response.getEmail());
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
        userWriteService.delete(userId);
        userCacheWriteService.delete(userId);
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
        final String findEmail = userReadService.findEmailByUserId(userId);
        final UserCache userCache = new UserCache(
            userId, findEmail, findEmail, FIXED_IMAGE_URL, NORMAL.name()
        );
        userCacheWriteService.save(userCache);
    }

    public void updateCache(
        final Long userId,
        final String nickname
    ) {
        final UserCache userCache = userCacheReadService.findById(userId);
        final UserCache updateUserCache = new UserCache(
            userId,
            nickname != null ? nickname : userCache.getNickname(),
            userCache.getEmail(),
            userCache.getProfileImageUrl(),
            userCache.getRole()
        );
        userCacheWriteService.save(updateUserCache);
    }

    public void updateUser(
        final Long userId,
        final UserUpdateCommand command
    ) {
        updateCache(userId, command.nickname());
    }
}
