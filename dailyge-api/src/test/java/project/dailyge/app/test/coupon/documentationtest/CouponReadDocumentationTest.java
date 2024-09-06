package project.dailyge.app.test.coupon.documentationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] 쿠폰 발급 참여 유효성 검증 문서화 테스트")
class CouponReadDocumentationTest extends DatabaseTestBase {
    private static final String USER_COUPON_KEY = "user:coupon";
    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.opsForValue().setBit(USER_COUPON_KEY, dailygeUser.getId(), false);
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 관련 쿠키가 없으면 쿠폰 발급 유효성 검증에서 200 응답을 받는다.")
    void whenDostNotHaveCouponCookieThenReturn200() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/coupons")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 관련 쿠키가 있으면 쿠폰 발급 유효성 검증에서 200 응답을 받는다.")
    void whenHaveCouponCookieThenReturn200() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .cookie(getCouponCookie())
            .when()
            .get("/api/coupons")
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
