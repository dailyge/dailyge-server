package project.dailyge.app.test.user.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import static project.dailyge.app.core.user.external.oauth.OAuthClient.GOOGLE;
import project.dailyge.app.core.user.external.response.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;

@DisplayName("[UnitTest] GoogleOAuthManager 단위 테스트")
class GoogleOAuthManagerUnitTest {

    @Test
    @DisplayName("개발 환경이 dev이면 목 데이터를 반환한다.")
    void whenEnvIsDevThenShouldReturnMockData() {
        final String mockEnv = "dev";
        final String authUrl = "mockAuthorizationUrl";
        final String userInfoUrl = "mockUserAccessUrl";
        final RestTemplate mockRestTemplate = mock(RestTemplate.class);
        final ClientRegistrationRepository mockClientRegistrationRepository = mock(ClientRegistrationRepository.class);

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
            () -> assertEquals("https://shorturl.at/dejs2", mockUserInfo.getPicture())
        );
    }

    @Test
    @DisplayName("로컬 환경에서는 로컬 목 데이터를 반환한다.")
    void whenEnvIsLocalThenShouldReturnLocalMockData() {
        final String mockEnv = "local";
        final String authUrl = "mockAuthorizationUrl";
        final String userInfoUrl = "mockUserAccessUrl";
        final RestTemplate mockRestTemplate = mock(RestTemplate.class);
        final ClientRegistrationRepository mockClientRegistrationRepository = mock(ClientRegistrationRepository.class);

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
            () -> assertEquals("dailyge@gmail.com", mockUserInfo.getEmail()),
            () -> assertEquals("https://shorturl.at/dejs2", mockUserInfo.getPicture()),
            () -> assertTrue(mockUserInfo.isVerifiedEmail())
        );
    }

    @Test
    @DisplayName("프로덕션 환경에서 실제 Google API 호출로 데이터를 반환한다.")
    void whenEnvIsProdThenShouldCallApiAndReturnUserInfo() {
        final String mockEnv = "prod";
        final String authUrl = "https://google.com/oauth2/token";
        final String userInfoUrl = "https://google.com/oauth2/userinfo";
        final RestTemplate mockRestTemplate = mock(RestTemplate.class);
        final ClientRegistrationRepository mockClientRegistrationRepository = mock(ClientRegistrationRepository.class);
        final ClientRegistration mockClientRegistration = mock(ClientRegistration.class);
        final GoogleAuthorizationResponse mockGoogleAuthorizationResponse = new GoogleAuthorizationResponse("mockAccessToken");

        when(mockClientRegistration.getAuthorizationGrantType())
            .thenReturn(AuthorizationGrantType.AUTHORIZATION_CODE);
        when(mockClientRegistrationRepository.findByRegistrationId(GOOGLE.getCode()))
            .thenReturn(mockClientRegistration);
        when(mockRestTemplate.postForObject(eq(authUrl), any(), eq(GoogleAuthorizationResponse.class)))
            .thenReturn(mockGoogleAuthorizationResponse);

        final ResponseEntity<GoogleUserInfoResponse> mockUserInfoResponse = new ResponseEntity<>(
            new GoogleUserInfoResponse("mockId", "mockName", "mockEmail", "mockPicture", true),
            HttpStatus.OK
        );

        when(mockRestTemplate.exchange(
            eq(userInfoUrl),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(GoogleUserInfoResponse.class))
        ).thenReturn(mockUserInfoResponse);

        final GoogleOAuthManager googleOAuthManager = new GoogleOAuthManager(
            mockEnv,
            authUrl,
            userInfoUrl,
            mockRestTemplate,
            mockClientRegistrationRepository
        );

        final GoogleUserInfoResponse userInfo = googleOAuthManager.getUserInfo("dummyCode");
        assertAll(
            () -> assertNotNull(userInfo),
            () -> assertEquals("mockId", userInfo.getId()),
            () -> assertEquals("mockName", userInfo.getName()),
            () -> assertEquals("mockEmail", userInfo.getEmail()),
            () -> assertEquals("mockPicture", userInfo.getPicture()),
            () -> assertTrue(userInfo.isVerifiedEmail())
        );
    }
}
