package project.dailyge.app.test.holiday.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.holiday.application.HolidayWriteService;
import static project.dailyge.app.test.holiday.documentationtest.snippet.HolidayReadSnippet.createHolidayReadFilter;
import static project.dailyge.app.test.holiday.documentationtest.snippet.HolidaySnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.holiday.documentationtest.snippet.HolidaySnippet.HOLIDAYS_READ_RESPONSE_SNIPPET;
import static project.dailyge.app.test.holiday.documentationtest.snippet.HolidaySnippet.HOLIDAY_YEAR_QUERY_PARAMS_SNIPPET;
import static project.dailyge.app.test.holiday.documentationtest.snippet.HolidaySnippet.createIdentifier;
import project.dailyge.entity.holiday.HolidayJpaEntity;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 공휴일 조회 문서화 테스트")
class HolidayReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private HolidayWriteService holidayWriteService;

    @Test
    @DisplayName("[RestDocs] 공휴일을 조회할 때, 공휴일이 존재하면 200 OK 응답 코드를 받는다.")
    void whenSearchHolidaysAndHolidaysExistsThenStatusCodeShouldBe_200_OK_RestDocs() {
        final LocalDate newYearDate = LocalDate.of(2024, 1, 1);
        final HolidayJpaEntity newHoliday = new HolidayJpaEntity(null, "새해", newYearDate, true, 1L);
        holidayWriteService.save(newHoliday);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                HOLIDAYS_READ_RESPONSE_SNIPPET,
                HOLIDAY_YEAR_QUERY_PARAMS_SNIPPET
            ))
            .when()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("year", newYearDate.getYear())
            .get("/api/holidays")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 공휴일을 조회할 때, 공휴일이 존재하면 200 OK 응답 코드를 받는다.")
    void whenSearchHolidaysAndHolidaysExistsThenStatusCodeShouldBe_200_OK_Swagger() {
        final LocalDate newYearDate = LocalDate.of(2024, 1, 1);
        final HolidayJpaEntity newHoliday = new HolidayJpaEntity(null, "새해", newYearDate, true, 1L);
        holidayWriteService.save(newHoliday);

        final RestDocumentationFilter filter = createHolidayReadFilter(createIdentifier("HolidaysRead", 200));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .when()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("year", newYearDate.getYear())
            .get("/api/holidays")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 보내면 400 Bad Request 응답 코드를 받는다.")
    void whenSearchHolidaysWithInvalidParametersThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final LocalDate newYearDate = LocalDate.of(2024, 1, 1);
        final HolidayJpaEntity newHoliday = new HolidayJpaEntity(null, "새해", newYearDate, true, 1L);
        holidayWriteService.save(newHoliday);

        final RestDocumentationFilter filter = createHolidayReadFilter(createIdentifier("HolidaysRead", 400));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .when()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("year", 0)
            .get("/api/holidays")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
