package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.common.exception.ExternalServerException.USER_INFO_API_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleUserInfoClient {

    private static final String BEARER = "Bearer ";

    private final RestTemplate restTemplate;

    @Value("${oauth.google.user-access}")
    private String userAccessUrl;

    public GoogleUserInfoResponse getUserInfo(final GoogleAuthorizationResponse response) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, BEARER + response.getAccessToken());
            final ResponseEntity<GoogleUserInfoResponse> userInfoResponse = restTemplate.exchange(
                userAccessUrl,
                GET,
                new HttpEntity<>(headers),
                GoogleUserInfoResponse.class
            );
            if (!OK.equals(userInfoResponse.getStatusCode())) {
                throw new ExternalServerException(USER_INFO_API_ERROR_MESSAGE, BAD_GATEWAY);
            }
            return userInfoResponse.getBody();
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
