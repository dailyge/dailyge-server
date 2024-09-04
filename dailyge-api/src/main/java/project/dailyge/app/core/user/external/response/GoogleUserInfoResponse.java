package project.dailyge.app.core.user.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfoResponse {

    private String id;
    private String name;
    private String email;
    private String picture;
    private boolean verifiedEmail;

    private GoogleUserInfoResponse() {
    }

    public GoogleUserInfoResponse(
        @JsonProperty(value = "id") final String id,
        @JsonProperty(value = "name") final String name,
        @JsonProperty(value = "email") final String email,
        @JsonProperty(value = "picture") final String picture,
        @JsonProperty(value = "verified_email") final boolean verifiedEmail
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.verifiedEmail = verifiedEmail;
    }

    @Override
    public String toString() {
        return "{"
            + "\"id\":\""
            + id
            + "\""
            + ", \"name\":\""
            + name
            + "\""
            + ", \"email\":\""
            + email
            + "\""
            + ", \"picture\":\""
            + picture
            + "\""
            + ", \"verified_email\":"
            + verifiedEmail
            + "}";
    }
}
