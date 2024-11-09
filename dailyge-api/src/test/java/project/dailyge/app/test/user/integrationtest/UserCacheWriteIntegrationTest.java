package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.common.EventType.UPDATE;

@DisplayName("[IntegrationTest] 사용자 캐시 저장 통합 테스트(with Blacklist)")
class UserCacheWriteIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserCacheReadService userCacheReadService;

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private UserFacade userFacade;

    @Test
    @DisplayName("사용자 캐시를 갱신 시, RDB의 사용자 데이터로 저장된다.")
    void whenSaveUserCacheThenResultShouldBeDatabaseUserInfo() {
        final UserJpaEntity newUser = new UserJpaEntity(null, "blacklistUser", "blacklistUser@gmail.com", "");
        final UserJpaEntity saveUser = userWriteService.save(newUser);

        final UserEvent userEvent = UserEvent.createEvent(saveUser.getId(), createTimeBasedUUID(), CREATE);
        userFacade.saveCache(userEvent);

        final UserCache userCache = userCacheReadService.findById(saveUser.getId());

        assertAll(
            () -> assertEquals(saveUser.getId(), userCache.getId()),
            () -> assertEquals(saveUser.getEmail(), userCache.getEmail()),
            () -> assertEquals(saveUser.getNickname(), userCache.getNickname()),
            () -> assertEquals(saveUser.getRoleAsString(), userCache.getRole())
        );
    }

    @Test
    @DisplayName("사용자 캐시를 UPDATE로 갱신할 경우, Cache가 비어있을 때만 갱신한다.")
    void whenCacheIsEmptyInUpdateThenCacheShouldBeSaved() {
        final UserJpaEntity newUser = new UserJpaEntity(null, "blacklistUser", "blacklistUser@gmail.com", "");
        final UserJpaEntity saveUser = userWriteService.save(newUser);

        final UserEvent userEvent = UserEvent.createEvent(saveUser.getId(), createTimeBasedUUID(), UPDATE);
        userFacade.saveCache(userEvent);

        final UserCache userCache = userCacheReadService.findById(saveUser.getId());

        assertAll(
            () -> assertEquals(saveUser.getId(), userCache.getId()),
            () -> assertEquals(saveUser.getEmail(), userCache.getEmail()),
            () -> assertEquals(saveUser.getNickname(), userCache.getNickname()),
            () -> assertEquals(saveUser.getRoleAsString(), userCache.getRole())
        );
    }
}
