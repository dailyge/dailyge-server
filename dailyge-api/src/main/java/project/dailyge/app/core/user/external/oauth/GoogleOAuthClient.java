package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.presentation.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.common.exception.ExternalServerException.*;

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
        final GoogleAuthorizationResponse authorizationResponse = getAuthorization(authorizationRequest);
        if (authorizationResponse == null) {
            throw new CommonException(BAD_GATEWAY);
        }

        final ResponseEntity<GoogleUserInfoResponse> userInfoResponse = getGoogleUserInfo(authorizationResponse);
        if (!userInfoResponse.getStatusCode().equals(OK)) {
            throw new CommonException(BAD_GATEWAY);
        }
        return userInfoResponse.getBody();
    }

    private GoogleAuthorizationResponse getAuthorization(final GoogleAuthorizationRequest request) {
        try {
            return restTemplate.postForObject(authorizationUrl, request, GoogleAuthorizationResponse.class);
        } catch (Exception ex) {
            throw new ExternalServerException(OAUTH_AUTHORIZATION_API_ERROR_MESSAGE, BAD_GATEWAY);
        }
    }

    private ResponseEntity<GoogleUserInfoResponse> getGoogleUserInfo(final GoogleAuthorizationResponse response) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, BEARER + response.getAccessToken());
            return restTemplate.exchange(userAccessUrl, GET, new HttpEntity<>(headers), GoogleUserInfoResponse.class);
        } catch (Exception ex) {
            throw new ExternalServerException(USER_INFO_API_ERROR_MESSAGE, BAD_GATEWAY);
        }
    }
}
