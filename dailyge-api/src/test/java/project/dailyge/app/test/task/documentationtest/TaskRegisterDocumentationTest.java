package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import static project.dailyge.app.test.user.fixture.UserFixture.createUserJpaEntity;
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
    private TaskFacade taskFacade;

    @Autowired
    private TaskDocumentReadRepository monthlyTaskReadRepository;

    @Test
    @DisplayName("할 일을 등록하면 201 Created 응답을 받는다.")
    void whenRegisterTaskThenStatusCodeShouldBe_201_RestDocs() throws Exception {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser.getId(), newUser.getRole());
        taskFacade.createMonthlyTasks(dailygeUser, LocalDate.now());
        final LocalDate now = LocalDate.now();
        final MonthlyTaskDocument findMonthlyTask = monthlyTaskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getUserId(), now).get();
        final TaskCreateRequest request = new TaskCreateRequest(findMonthlyTask.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_CREATE_REQUEST_SNIPPET,
                TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(201);
    }
}
