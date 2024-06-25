package project.dailyge.app.core.user.presentation.request.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthorizationRequest {

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    private String code;

    @JsonProperty(value = "grant_type")
    private String grantType;

    @JsonProperty(value = "redirect_uri")
    private String redirectUri;

    public GoogleAuthorizationRequest(
        final String code,
        final ClientRegistration clientRegistration
    ) {
        this.code = code;
        this.clientId = clientRegistration.getClientId();
        this.clientSecret = clientRegistration.getClientSecret();
        this.grantType = clientRegistration.getAuthorizationGrantType().getValue();
        this.redirectUri = clientRegistration.getRedirectUri();
    }
}
