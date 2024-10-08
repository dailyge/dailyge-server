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
import project.dailyge.app.core.task.application.TaskReadService;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskCreateSnippet.createTasksFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.entity.task.TaskColor.BLUE;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 등록 문서화 테스트")
class TaskCreateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadService taskReadService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
    }

    @Test
    @DisplayName("[RestDocs] Task를 등록하면 201 Created 응답을 받는다.")
    void whenRegisterTaskThenStatusCodeShouldBe_201_RestDocs() throws Exception {
        final TaskCreateRequest request = new TaskCreateRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", BLUE, now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_CREATE_REQUEST_SNIPPET,
                TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] Task를 등록하면 201 Created 응답을 받는다.")
    void whenRegisterTaskThenStatusCodeShouldBe_201_Swagger() throws Exception {
        final TaskCreateRequest request = new TaskCreateRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", BLUE, now);
        final RestDocumentationFilter filter = createTasksFilter(createIdentifier("TaskCreate", 201));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터로 Task를 생성하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws Exception {
        final TaskCreateRequest request = new TaskCreateRequest("주간 미팅", null, BLUE, now);
        final RestDocumentationFilter filter = createTasksFilter(createIdentifier("TaskCreate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(400);
    }
}
