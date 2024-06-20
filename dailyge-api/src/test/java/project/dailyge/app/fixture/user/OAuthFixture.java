package project.dailyge.app.fixture.user;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OAuthFixture {

    private OAuthFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Map<String, String> getOAuthAccessResponseFixture() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/resources/mappings/GoogleOAuthUserInfo_OK.json");

        Map<String, Object> map = objectMapper.readValue(jsonFile, Map.class);
        Map<String, Object> response = (Map<String, Object>) map.get("response");
        return (Map<String, String>) response.get("jsonBody");
    }
}
