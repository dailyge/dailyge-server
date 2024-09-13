package project.dailyge.app.test.coupon.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.coupon.presentation.request.CouponWinnerRequest;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] 쿠폰 당첨자 선정 문서화 테스트")
class CouponWinnerDocumentationTest extends DatabaseTestBase {
    @Autowired
    private CouponEventWriteRepository repository;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;
    private CouponWinnerRequest request;

    @BeforeEach
    void setUp() {
        request = new CouponWinnerRequest(1000, 1L);
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.serverCommands().flushAll();
            return null;
        });
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 당첨자 선정 작업이 일어나면 200 응답을 받는다.")
    void whenCouponWinnerSelectionRunsThenReturn200() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(request))
            .post("/api/coupons/winners")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 당첨자 선정 작업이 이전에 실행됐다면 409 응답을 받는다.")
    void whenCouponWinnerSelectionAlreadyExistsThenReturn409() throws JsonProcessingException {
        repository.increaseSelectionRunCount();
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(request))
            .post("/api/coupons/winners")
            .then()
            .statusCode(409)
            .log()
            .all();
    }
}
