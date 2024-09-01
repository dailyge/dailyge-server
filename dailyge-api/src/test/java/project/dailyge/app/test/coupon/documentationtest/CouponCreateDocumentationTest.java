package project.dailyge.app.test.coupon.documentationtest;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;
import static project.dailyge.app.test.coupon.documentationtest.snippet.CouponSnippet.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;

import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.presentation.request.CouponCreateRequest;

@DisplayName("[DocumentationTest] 쿠폰 발급 신청 문서화 테스트")
public class CouponCreateDocumentationTest extends DatabaseTestBase {
	@Autowired
	private CouponWriteUseCase couponWriteUseCase;

	@BeforeEach
	void setUp() {
		nowTime = LocalDateTime.now();

	}
	@Test
	@DisplayName("[RestDocs] 쿠폰 발급에 참여하면 201 Created 응답을 받는다.")
	void whenCreateCouponThenResponseShouldBe201_RestDocs() throws JsonProcessingException {
		final CouponCreateRequest request = new CouponCreateRequest(nowTime);
		given(this.specification)
			.relaxedHTTPSValidation()
			.filter(document(IDENTIFIER,
				COUPON_AUTHORIZATION_HEADER,
				COUPON_CREATE_REQUEST_SNIPPET,
				COUPON_CREATE_RESPONSE_SNIPPET
			))
			.contentType(APPLICATION_JSON_VALUE)
			.header(AUTHORIZATION, getAuthorizationHeader())
			.body(objectMapper.writeValueAsString(request))
			.when()
			.post("/api/coupons")
			.then()
			.statusCode(201);
	}
}
