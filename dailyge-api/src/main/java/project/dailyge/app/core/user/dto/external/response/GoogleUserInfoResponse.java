package project.dailyge.app.core.user.dto.external.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfoResponse {

    private String id;

    private String name;

    private String email;

    private String picture;

    @JsonProperty(value = "verified_email")
    private boolean verifiedEmail;
}
