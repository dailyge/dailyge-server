package project.dailyge.app.test.task.documentationtest;

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
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_RESPONSE_SNIPPET;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 할 일 등록 문서화 테스트")
class TaskRegisterDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskDocumentReadRepository monthlyTaskReadRepository;

    @Test
    @ResourceLock(value = "restDocs", mode = ResourceAccessMode.READ_WRITE)
    @DisplayName("할 일을 등록하면 201 Created 응답을 받는다.")
    void whenRegisterTaskThenStatusCodeShouldBe_201() throws Exception {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        taskFacade.createMonthlyTasks(dailygeUser, LocalDate.now());
        final LocalDate now = LocalDate.now();
        final MonthlyTaskDocument findMonthlyTask = monthlyTaskReadRepository.findMonthlyDocumentByUserId(dailygeUser.getUserId(), now).get();
        final TaskRegisterRequest request = new TaskRegisterRequest(findMonthlyTask.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_CREATE_REQUEST_SNIPPET,
                TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(201);
    }
}
