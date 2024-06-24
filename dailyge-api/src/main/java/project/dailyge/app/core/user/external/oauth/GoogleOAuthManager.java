package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.presentation.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.common.exception.ExternalServerException.USER_INFO_API_ERROR_MESSAGE;
import static project.dailyge.app.core.user.external.oauth.OAuthClient.GOOGLE;

@Component
@RequiredArgsConstructor
public class GoogleOAuthManager {

    @Value("${oauth.google.authorization}")
    private String authorizationUrl;
    @Value("${oauth.google.user-access}")
    private String userAccessUrl;
    private static final String BEARER = "Bearer ";

    private final RestTemplate restTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public GoogleUserInfoResponse login(final String code) {
        final GoogleAuthorizationResponse response = userAccess(code);
        return getUserInfo(response);
    }

    private GoogleAuthorizationResponse userAccess(final String code) {
        final ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(GOOGLE.getCode());
        final GoogleAuthorizationRequest authorizationRequest = new GoogleAuthorizationRequest(code, clientRegistration);
        try {
            return restTemplate.postForObject(authorizationUrl, authorizationRequest, GoogleAuthorizationResponse.class);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }

    private GoogleUserInfoResponse getUserInfo(final GoogleAuthorizationResponse response) {
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
