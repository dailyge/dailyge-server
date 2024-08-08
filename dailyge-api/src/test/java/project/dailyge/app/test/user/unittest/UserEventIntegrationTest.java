package project.dailyge.app.test.user.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import project.dailyge.app.core.user.application.service.UserCacheWriteService;
import project.dailyge.app.core.user.facade.UserEventHandler;
import project.dailyge.app.core.user.persistence.UserCacheWriteDao;
import project.dailyge.core.cache.user.UserCache;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;

@DisplayName("[UnitTest] UserEvent 통합 테스트")
class UserEventIntegrationTest {

    @Test
    @DisplayName("userEvent를 받아 실행하면, UserCache가 저장된다.")
    void whenUserEventReceivedAndExecuteThenUserCacheShouldBeSave() throws InterruptedException {
        final UserCacheWriteDao userCacheWriteDao = mock(UserCacheWriteDao.class);
        final UserCacheWriteService userCacheWriteService = new UserCacheWriteService(userCacheWriteDao);
        final UserEventHandler userEventHandler = new UserEventHandler(userCacheWriteService);
        final UserEvent userEvent = UserEvent.createEvent(
            1L,
            "eventId",
            CREATE,
            "nickname",
            "dailyge@gmail.com",
            "user.jpg"
        );
        userEventHandler.onUserEvent(userEvent);
        final UserCache userCache = new UserCache(
            userEvent.getPublisher(),
            userEvent.getNickname(),
            userEvent.getEmail(),
            userEvent.getProfileImageUrl()
        );
        verify(userCacheWriteDao, times(1)).save(userCache);
    }
}
