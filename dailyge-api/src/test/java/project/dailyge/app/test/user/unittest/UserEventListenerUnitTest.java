package project.dailyge.app.test.user.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.listener.UserEventListener;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.UserEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.dailyge.app.test.user.fixture.UserFixture.ID;
import static project.dailyge.app.test.user.fixture.UserFixture.user;
import static project.dailyge.app.test.user.fixture.UserFixture.userCache;
import static project.dailyge.entity.common.EventType.CREATE;

@DisplayName("[UnitTest] UserEventListener 단위 테스트")
class UserEventListenerUnitTest {

    private UserCacheReadUseCase userCacheReadUseCase;
    private UserCacheWriteUseCase userCacheWriteUseCase;
    private UserReadUseCase userReadUseCase;
    private UserWriteUseCase userWriteUseCase;
    private UserEventListener userEventListener;

    @BeforeEach
    void setUp() {
        userCacheReadUseCase = mock(UserCacheReadUseCase.class);
        userCacheWriteUseCase = mock(UserCacheWriteUseCase.class);
        userReadUseCase = mock(UserReadUseCase.class);
        userWriteUseCase = mock(UserWriteUseCase.class);
        userEventListener = new UserEventListener(
            userCacheReadUseCase,
            userCacheWriteUseCase,
            userReadUseCase,
            userWriteUseCase
        );
    }

    @Test
    @DisplayName("캐시에 없다면, DB에서 가져와 저장한다.")
    void whenCacheIsEmptyThenCacheShouldBeResave() {
        when(userCacheReadUseCase.findById(ID)).thenReturn(null);
        when(userReadUseCase.findById(ID)).thenReturn(user);
        final UserEvent event = UserEvent.createEvent(ID, "123", CREATE);
        userEventListener.listenEvent(event);

        verify(userCacheWriteUseCase).save(userCache);
    }

    @Test
    @DisplayName("캐시에 등록된 사용자는, 캐시 유효기간만 갱신한다.")
    void whenRegisteredUserLoginThenCacheValidityShouldBeRefresh() {
        when(userCacheReadUseCase.findById(ID)).thenReturn(userCache);
        when(userReadUseCase.existsById(ID)).thenReturn(true);
        final UserEvent event = UserEvent.createEvent(ID, "123", CREATE);
        userEventListener.listenEvent(event);

        verify(userCacheWriteUseCase).refreshExpirationDate(ID);
    }

    @Test
    @DisplayName("최초 가입 시, DB에 저장한다.")
    void whenFirstSignUpThenUserShouldBeSaveToDB() {
        when(userCacheReadUseCase.findById(ID)).thenReturn(userCache);
        when(userReadUseCase.existsById(ID)).thenReturn(false);

        final UserEvent event = UserEvent.createEvent(ID, "123", CREATE);
        userEventListener.listenEvent(event);

        verify(userWriteUseCase).save(user);
    }
}
