package project.dailyge.app.common.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.UnAuthorizedException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;

@Component
@RequiredArgsConstructor
public class SecretKeyManager {

    private SecretKeySpec secretKeySpec;
    private final JwtProperties jwtProperties;

    public SecretKeySpec getSecretKeySpec() {
        if (secretKeySpec == null) {
            synchronized (SecretKeyManager.class) {
                if (secretKeySpec == null) {
                    try {
                        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                        final KeySpec spec = new PBEKeySpec(
                            jwtProperties.getPayloadSecretKey().toCharArray(),
                            jwtProperties.getSalt().getBytes(),
                            65536,
                            256
                        );
                        final SecretKey secretKey = secretKeyFactory.generateSecret(spec);
                        secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
                    } catch (Exception ex) {
                        throw new UnAuthorizedException(INVALID_USER_TOKEN);
                    }
                }
            }
        }
        return secretKeySpec;
    }
}
