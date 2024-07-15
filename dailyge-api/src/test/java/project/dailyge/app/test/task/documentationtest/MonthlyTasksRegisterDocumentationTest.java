package project.dailyge.app.test.task.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.presentation.requesst.MonthlyTasksRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 월간 일정표 생성 문서화 테스트")
class MonthlyTasksRegisterDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @ResourceLock(value = "restDocs", mode = ResourceAccessMode.READ_WRITE)
    @DisplayName("월간 일정표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateMonthlyTasksThenResponseShouldBe201() throws JsonProcessingException {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate now = LocalDate.now();
        final MonthlyTasksRegisterRequest request = new MonthlyTasksRegisterRequest(now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                MONTHLY_TASK_CREATE_REQUEST_SNIPPET,
                MONTHLY_TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .header(USER_ID_KEY, dailygeUser.getUserId())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/monthly-tasks")
            .then()
            .statusCode(201);
    }
}
