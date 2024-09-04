package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskDeleteSnippet.createTaskDeleteFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.entity.task.TaskColor.BLUE;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 삭제 문서화 테스트")
class TaskDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
    }

    @Test
    @DisplayName("[RestDocs] Task를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteTaskThenStatusCodeShouldBe_204_RestDocs() {
        final TaskCreateRequest request = new TaskCreateRequest(
            "주간 미팅", "Backend 팀과 Api 스펙 정의", BLUE, now
        );
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, request.toCommand());

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_PATH_PARAMETER_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .header(USER_ID_KEY, newUser.getId())
            .when()
            .delete("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] Task를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteTaskThenStatusCodeShouldBe_204_Swagger() {
        final TaskCreateRequest request = new TaskCreateRequest(
            "주간 미팅", "Backend 팀과 Api 스펙 정의", BLUE, now
        );
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDeleteFilter(
            createIdentifier("MonthlyTaskDelete", 204)
        );

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .header(USER_ID_KEY, newUser.getId())
            .when()
            .delete("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }
}
