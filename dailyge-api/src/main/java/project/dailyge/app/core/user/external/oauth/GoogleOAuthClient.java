package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.dto.external.request.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.dto.external.response.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.dto.external.response.GoogleUserInfoResponse;

import static org.springframework.http.HttpMethod.GET;

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
        GoogleAuthorizationRequest request = new GoogleAuthorizationRequest(code, clientRegistration);
        GoogleAuthorizationResponse authorizationResponse =
            restTemplate.postForObject(authorizationUrl, request, GoogleAuthorizationResponse.class);

        if (authorizationResponse == null) {
            throw new CommonException(CommonCodeAndMessage.BAD_GATEWAY);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER + authorizationResponse.getAccessToken());
        ResponseEntity<GoogleUserInfoResponse> userInfoResponse =
            restTemplate.exchange(userAccessUrl, GET, new HttpEntity<>(headers), GoogleUserInfoResponse.class);

        if (!userInfoResponse.getStatusCode().equals(HttpStatus.OK)) {
            throw new CommonException(CommonCodeAndMessage.BAD_GATEWAY);
        }

        return userInfoResponse.getBody();
    }
}
