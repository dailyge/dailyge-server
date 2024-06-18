package project.dailyge.app.test.common;

import jakarta.servlet.http.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import org.springframework.web.context.request.*;
import project.dailyge.app.common.auth.*;
import project.dailyge.app.common.exception.*;
import project.dailyge.app.common.configuration.web.AuthArgumentResolver;
import project.dailyge.app.core.user.application.*;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import project.dailyge.domain.user.*;

@DisplayName("[UnitTest] AuthArgumentResolver 검증 단위 테스트")
class AuthArgumentResolverTest {

    private AuthArgumentResolver resolver;
    private UserReadUseCase userReadUseCase;
    private NativeWebRequest webRequest;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userReadUseCase = mock(UserReadUseCase.class);
        resolver = new AuthArgumentResolver(userReadUseCase);
        request = mock(HttpServletRequest.class);
        webRequest = mock(NativeWebRequest.class);
        when(webRequest.getNativeRequest())
            .thenReturn(request);
    }

    @Test
    @DisplayName("사용자 ID가 존재하고, 올바르다면 인증 객체가 생성된다.")
    void shouldBeNotNullWhenUserIdIsValid() {
        UserJpaEntity user = createUserJpaEntity(1L);
        when(request.getHeader("dailyge_user_id"))
            .thenReturn(user.getId().toString());
        when(userReadUseCase.findAuthorizedById(user.getId()))
            .thenReturn(user);

        DailygeUser result = (DailygeUser) resolver.resolveArgument(null, null, webRequest, null);

        assertNotNull(result);
    }

    @Test
    @DisplayName("사용자 ID가 null이면 UnAuthorizedException이 발생한다.")
    void shouldThrowUnAuthorizedExceptionWhenUserIdIsNull() {
        when(request.getHeader("dailyge_user_id"))
            .thenReturn(null);

        assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("사용자 ID가 올바르지 않은 타입이면 UnAuthorizedException이 발생한다.")
    void shouldThrowUnAuthorizedExceptionWhenUserIdIsInvalidType() {
        when(request.getHeader("dailyge_user_id"))
            .thenReturn("abc");

        assertThatThrownBy(() -> resolver.resolveArgument(null, null, webRequest, null))
            .isExactlyInstanceOf(UnAuthorizedException.class)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("사용자 ID가 유효하면 예외가 발생하지 않는다.")
    void shouldNotThrowExceptionWhenUserIdIsValid() {
        String validUserId = "456";
        UserJpaEntity expectedUser = createUserJpaEntity(456L);
        when(request.getHeader("dailyge_user_id"))
            .thenReturn(validUserId);
        when(userReadUseCase.findAuthorizedById(Long.parseLong(validUserId)))
            .thenReturn(expectedUser);

        assertDoesNotThrow(
            () -> ((DailygeUser) resolver.resolveArgument(null, null, webRequest, null))
        );
    }
}
