package project.dailyge.app.test.user.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;

@DisplayName("[UnitTest] AuthManager 단위 테스트")
class GoogleOAuthManagerUnitTest {

    @Test
    @DisplayName("개발 환경이 dev이면 목 데이터를 반환한다.")
    void whenEnvIsDevThenShouldReturnMockData() {
        final String mockEnv = "dev";
        final String authUrl = "mockAuthorizationUrl";
        final String userInfoUrl = "mockUserAccessUrl";
        final RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        final ClientRegistrationRepository mockClientRegistrationRepository = Mockito.mock(ClientRegistrationRepository.class);

        final GoogleOAuthManager googleOAuthManager = new GoogleOAuthManager(
            mockEnv,
            authUrl,
            userInfoUrl,
            mockRestTemplate,
            mockClientRegistrationRepository
        );

        final GoogleUserInfoResponse mockUserInfo = googleOAuthManager.getUserInfo("dummyCode");

        assertAll(
            () -> assertNotNull(mockUserInfo),
            () -> assertNotNull(mockUserInfo.getId()),
            () -> assertNotNull(mockUserInfo.getName()),
            () -> assertNotNull(mockUserInfo.getEmail()),
            () -> assertNotNull(mockUserInfo.getPicture()),
            () -> assertTrue(mockUserInfo.isVerifiedEmail()),
            () -> assertEquals(mockUserInfo.getName(), mockUserInfo.getEmail()),
            () -> assertEquals("https://shorturl.at/dejs2", mockUserInfo.getPicture())
        );
    }
}
