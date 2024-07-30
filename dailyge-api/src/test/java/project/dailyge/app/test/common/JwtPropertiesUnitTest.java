package project.dailyge.app.test.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.auth.JwtProperties;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("[UnitTest] JwtProperties 단위 테스트")
class JwtPropertiesUnitTest {

    private static final String PAYLOAD_SECRET_KEY = "payloadSecretKey";
    private static final String SALT = "salt";
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties(
            "secretKey",
            PAYLOAD_SECRET_KEY,
            SALT,
            1,
            2
        );
    }

    @Test
    @DisplayName("PayloadSecretKey 를 Chars 로 조회 시, 정상적으로 Chars 로 조회 된다.")
    void whenGetPayloadSecretKeyAsCharsThenResultShouldBeChars() {
        final char[] payloadSecretKeyChars = jwtProperties.getPayloadSecretKeyChars();

        assertArrayEquals(PAYLOAD_SECRET_KEY.toCharArray(), payloadSecretKeyChars);
    }

    @Test
    @DisplayName("SALT 를 Bytes 로 조회 시, 정상적으로 Bytes 로 조회 된다.")
    void whenGetSaltAsBytesThenResultShouldBeBytes() {
        final byte[] payloadSecretKeyChars = jwtProperties.getSaltBytes();

        assertArrayEquals(SALT.getBytes(), payloadSecretKeyChars);
    }
}
