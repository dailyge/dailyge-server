package project.dailyge.app.test.user.fixture;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import project.dailyge.app.test.user.fixture.OAuthUserInfoJson.Response;
import project.dailyge.app.test.user.fixture.OAuthUserInfoJson.ResponseBody;

import java.io.File;
import java.io.IOException;

public final class OAuthFixture {

    private OAuthFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static ResponseBody getOAuthAccessResponseBodyFixture() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final File jsonFile = new File("src/test/resources/mappings/GoogleOAuthUserInfo_OK.json");

        final OAuthUserInfoJson userInfoJson = objectMapper.readValue(jsonFile, OAuthUserInfoJson.class);
        final Response response = userInfoJson.getResponse();
        return response.getBody();
    }
}
