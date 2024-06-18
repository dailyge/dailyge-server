package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.presentation.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuthClient {

    private static final String BEARER = "Bearer ";

    private final RestTemplate restTemplate;

    @Value("${oauth.google.authorization}")
    private String authorizationUrl;

    @Value("${oauth.google.user-access}")
    private String userAccessUrl;

    public GoogleUserInfoResponse getAccessToken(
        final ClientRegistration clientRegistration,
        final String code
    ) {
        final GoogleAuthorizationRequest authorizationRequest = new GoogleAuthorizationRequest(code, clientRegistration);
        final GoogleAuthorizationResponse authorizationResponse = googleAuthorization(authorizationRequest);
        if (authorizationResponse == null) {
            throw new CommonException(CommonCodeAndMessage.BAD_GATEWAY);
        }

        final ResponseEntity<GoogleUserInfoResponse> userInfoResponse = getGoogleUserInfo(authorizationResponse);
        if (userInfoResponse == null || !userInfoResponse.getStatusCode().equals(HttpStatus.OK)) {
            throw new CommonException(CommonCodeAndMessage.BAD_GATEWAY);
        }
        return userInfoResponse.getBody();
    }

    private GoogleAuthorizationResponse googleAuthorization(GoogleAuthorizationRequest request) {
        try {
            return restTemplate.postForObject(authorizationUrl, request, GoogleAuthorizationResponse.class);
        } catch (Exception e) {
            log.error("Google OAuth2 권한 인증 예외:{}", e.getMessage());
        }
        return null;
    }

    private ResponseEntity<GoogleUserInfoResponse> getGoogleUserInfo(GoogleAuthorizationResponse authorizationResponse) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, BEARER + authorizationResponse.getAccessToken());
            return restTemplate.exchange(userAccessUrl, GET, new HttpEntity<>(headers), GoogleUserInfoResponse.class);
        } catch (Exception e) {
            log.error("Google OAuth2 사용자 정보 조회 예외:{}", e.getMessage());
        }
        return null;
    }
}
