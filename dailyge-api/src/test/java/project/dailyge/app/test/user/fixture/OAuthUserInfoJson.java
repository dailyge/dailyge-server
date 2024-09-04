package project.dailyge.app.test.user.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class OAuthUserInfoJson {

    @JsonProperty("request")
    private Request request;

    @JsonProperty("response")
    private Response response;

    public OAuthUserInfoJson() {
    }

    public Response getResponse() {
        return response;
    }

    public static class Request {

        @JsonProperty("method")
        private String method;

        @JsonProperty("urlPath")
        private String urlPath;

        public Request() {
        }
    }

    public static class Response {

        @JsonProperty("headers")
        private Map<String, String> headers;

        @JsonProperty("status")
        private int status;

        @JsonProperty("jsonBody")
        private ResponseBody body;

        public Response() {
        }

        public ResponseBody getBody() {
            return body;
        }
    }

    public static class ResponseBody {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("picture")
        private String picture;

        public ResponseBody() {
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPicture() {
            return picture;
        }
    }
}
