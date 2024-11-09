package project.dailyge.app.test.user.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.entity.user.UserJpaEntity;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.DUPLICATED_EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;

@DisplayName("[IntegrationTest] 사용자 저장 통합 테스트")
class UserCreateIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    @DisplayName("사용자 등록이 정상적으로 성공하면, 사용자 ID는 NULL이 아니다.")
    void whenUserSaveSuccessThenUserIdShouldBeNotNull() {
        final UserJpaEntity saveUser = userWriteService.save(createUser(null, "dailyges", "dailyges@gmail.com"));

        assertNotNull(saveUser.getId());
    }

    @Test
    @DisplayName("사용자 등록 시 삭제되지 않은 동일한 이메일이 있을 경우, DuplicatedEmailException이 발생한다.")
    void whenUserEmailDuplicatedThenDuplicatedEmailExceptionShouldBeHappen() {
        assertThatThrownBy(() -> userWriteService.save(newUser))
            .isExactlyInstanceOf(UserTypeException.from(DUPLICATED_EMAIL).getClass())
            .isInstanceOf(UserTypeException.class)
            .hasMessage(DUPLICATED_EMAIL.message());
    }

    @Test
    @DisplayName("최초 로그인 시, 사용자가 회원가입된다.")
    void whenFirstLoginThenUserIsShouldBeRegister() throws JsonProcessingException {
        final String name = "dailyge";
        final String email = "dailyge@gmail.com";
        final GoogleUserInfoResponse response = new GoogleUserInfoResponse(name, name, email, "https://shorturl.at/dejs2", true);

        stubFor(get(urlEqualTo("/userinfo"))
            .willReturn(aResponse()
                .withStatus(BAD_GATEWAY.value())
                .withStatusMessage(BAD_GATEWAY.getReasonPhrase())
                .withBody(objectMapper.writeValueAsString(response))));

        userFacade.login("AuthenticationCode");

        assertNotNull(userReadService.findUserIdByEmail(email));
    }
}
