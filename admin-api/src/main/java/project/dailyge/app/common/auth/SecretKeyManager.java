package project.dailyge.app.common.auth;

import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyManager {

    private final SecretKeySpec secretKeySpec;

    public SecretKeyManager(final JwtProperties jwtProperties) {
        try {
            final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec spec = new PBEKeySpec(
                jwtProperties.getPayloadSecretKeyChars(),
                jwtProperties.getSaltBytes(),
                600000,
                256
            );
            final SecretKey secretKey = secretKeyFactory.generateSecret(spec);
            secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }
}
