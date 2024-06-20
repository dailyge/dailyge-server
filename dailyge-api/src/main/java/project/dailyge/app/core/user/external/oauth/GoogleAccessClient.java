package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.presentation.GoogleAuthorizationRequest;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;

@Component
@RequiredArgsConstructor
public class GoogleAccessClient {

    @Value("${oauth.google.authorization}")
    private String authorizationUrl;

    private final RestTemplate restTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public GoogleAuthorizationResponse userAccess(final String code) {
        final ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        final GoogleAuthorizationRequest authorizationRequest = new GoogleAuthorizationRequest(code, clientRegistration);
        try {
            return restTemplate.postForObject(authorizationUrl, authorizationRequest, GoogleAuthorizationResponse.class);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
