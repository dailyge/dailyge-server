package project.dailyge.app.core.user.external.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

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

    private GoogleAuthorizationRequest() {
    }

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

    public GoogleAuthorizationRequest(
        final String clientId,
        final String clientSecret,
        final String code,
        final String grantType,
        final String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCode() {
        return code;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
