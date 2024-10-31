package project.dailyge.app.core.user.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAuthorizationResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    private String scope;

    @JsonProperty(value = "token_type")
    private String tokenType;

    private GoogleAuthorizationResponse() {
    }

    public GoogleAuthorizationResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public GoogleAuthorizationResponse(
        final String accessToken,
        final Long expiresIn,
        final String scope,
        final String tokenType
    ) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
