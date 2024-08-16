package project.dailyge.app.core.user.external.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import project.dailyge.app.common.annotation.ExternalLayer;
import project.dailyge.app.common.exception.ExternalServerException;
import static project.dailyge.app.core.user.external.oauth.OAuthClient.GOOGLE;
import project.dailyge.app.core.user.external.request.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.external.response.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;

import java.util.UUID;

@ExternalLayer
public class GoogleOAuthManager {

    private static final String BEARER = "Bearer ";

    private final String env;
    private final String authorizationUrl;
    private final String userAccessUrl;
    private final RestTemplate restTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public GoogleOAuthManager(
        @Value("${env}") final String env,
        @Value("${oauth.google.authorization}") final String authorizationUrl,
        @Value("${oauth.google.user-access}") final String userAccessUrl,
        final RestTemplate restTemplate,
        final ClientRegistrationRepository clientRegistrationRepository
    ) {
        this.env = env;
        this.authorizationUrl = authorizationUrl;
        this.userAccessUrl = userAccessUrl;
        this.restTemplate = restTemplate;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public GoogleUserInfoResponse getUserInfo(final String code) {
        if (env.equals("dev") || env.equals("test")) {
            return returnMockUserInfo();
        }
        final GoogleAuthorizationResponse response = getAccessToken(code);
        return getUserInfo(response);
    }

    private GoogleUserInfoResponse returnMockUserInfo() {
        final String uuid = UUID.randomUUID().toString();
        final String name = uuid.substring(1, 25).replaceAll("-", "");
        final String email = name + "@gmail.com";
        final String imageUrl = "https://shorturl.at/dejs2";
        return new GoogleUserInfoResponse(uuid, name, email, imageUrl, true);
    }

    private GoogleAuthorizationResponse getAccessToken(final String code) {
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
            return userInfoResponse.getBody();
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
