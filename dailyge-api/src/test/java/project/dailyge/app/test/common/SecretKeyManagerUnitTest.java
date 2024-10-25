package project.dailyge.app.test.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.common.auth.JwtProperties;
import project.dailyge.app.core.common.auth.SecretKeyManager;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] SecretKeyManager 단위 테스트")
class SecretKeyManagerUnitTest {

    private static final String PAYLOAD_SECRET_KEY = "payloadSecretKey";
    private static final String SALT = "salt";
    private JwtProperties jwtProperties;
    private SecretKeyManager secretKeyManager;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties(
            null,
            PAYLOAD_SECRET_KEY,
            SALT,
            0,
            0
        );
        secretKeyManager = new SecretKeyManager(jwtProperties);
    }

    @Test
    @DisplayName("SecretKeySpec 를 호출하면, 설정한 조건의 SecretKeySpec 이 반환된다.")
    void whenSecretKeySpecCallThenResultShouldBeConditionsSetSecretKeySpec() throws InvalidKeySpecException, NoSuchAlgorithmException {
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final KeySpec spec = new PBEKeySpec(
            jwtProperties.getPayloadSecretKeyChars(),
            jwtProperties.getSaltBytes(),
            600000,
            256
        );
        final SecretKey secretKey = secretKeyFactory.generateSecret(spec);
        final SecretKeySpec expectedSecretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        final SecretKeySpec secretKeySpec = secretKeyManager.getSecretKeySpec();

        assertEquals(expectedSecretKeySpec, secretKeySpec);
    }

    @Test
    @DisplayName("PayloadSecretKey 가 다르면, 다른 SecretKeySpec 이 반환된다.")
    void whenOtherSecretKeyThenResultShouldBeDifferentSecretKeySpec() {
        final SecretKeySpec originalSecretKeySpec = secretKeyManager.getSecretKeySpec();

        final JwtProperties otherSecretKeysProperties = new JwtProperties(
            null,
            "otherPayloadSecretKey",
            SALT,
            0,
            0
        );
        secretKeyManager = new SecretKeyManager(otherSecretKeysProperties);
        final SecretKeySpec otherSecretKeySpec = secretKeyManager.getSecretKeySpec();

        assertNotEquals(originalSecretKeySpec, otherSecretKeySpec);
    }

    @Test
    @DisplayName("Salt 가 다르면, 다른 SecretKeySpec 이 반환된다.")
    void whenOtherSaltThenResultShouldBeDifferentSecretKeySpec() {
        final SecretKeySpec originalSecretKeySpec = secretKeyManager.getSecretKeySpec();

        final JwtProperties otherSaltProperties = new JwtProperties(
            null,
            PAYLOAD_SECRET_KEY,
            "otherSalt",
            0,
            0
        );
        secretKeyManager = new SecretKeyManager(otherSaltProperties);
        final SecretKeySpec otherSecretKeySpec = secretKeyManager.getSecretKeySpec();

        assertNotEquals(originalSecretKeySpec, otherSecretKeySpec);
    }
}
