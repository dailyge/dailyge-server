package project.dailyge.app.test.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import project.dailyge.app.common.configuration.web.CorsFilter;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;

@DisplayName("[UnitTest] CorsFilter 단위 테스트")
class CorsFilterUnitTest {

    private static final String CLIENT_URL = "http://localhost:3000";

    private CorsFilter corsFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        corsFilter = new CorsFilter();
    }

    @Test
    @DisplayName("클라이언트가 요청했을 때, 서버가 세팅한 응답이 내려온다.")
    void whenClientRequestThenResultShouldBeCorsServerSetting() throws ServletException, IOException {
        when(request.getHeader(ORIGIN)).thenReturn(CLIENT_URL);

        corsFilter.doFilter(request, response, filterChain);

        verify(response).setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, CLIENT_URL);
        verify(response).setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        verify(response).setHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
        verify(response).setHeader(ACCESS_CONTROL_MAX_AGE, "600");
        verify(response).setHeader(ACCESS_CONTROL_ALLOW_HEADERS,
            String.join(", ", ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, "X-Requested-With"));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("요청 메서드가 Options일 경우, doFilter가 호출되지 않는다.")
    void whenRequestMethodIsOptionsThenDoFilterShouldBeNotCalled() throws ServletException, IOException {
        when(request.getMethod()).thenReturn(HttpMethod.OPTIONS.name());

        corsFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(OK.code());
        verify(filterChain, never()).doFilter(request, response);
    }
}
