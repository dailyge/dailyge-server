package project.dailyge.app.core.common.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    private final String secretKey;
    private final String payloadSecretKey;
    private final String salt;
    private final int accessExpiredTime;
    private final int refreshExpiredTime;

    public JwtProperties(
        @Value("${jwt.secret-key}") final String secretKey,
        @Value("${jwt.payload-secret-key}") final String payloadSecretKey,
        @Value("${jwt.salt}") final String salt,
        @Value("${jwt.access-expired-time}") final int accessExpiredTime,
        @Value("${jwt.refresh-expired-time}") final int refreshExpiredTime
    ) {
        this.secretKey = secretKey;
        this.payloadSecretKey = payloadSecretKey;
        this.salt = salt;
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPayloadSecretKey() {
        return payloadSecretKey;
    }

    public String getSalt() {
        return salt;
    }

    public int getAccessExpiredTime() {
        return accessExpiredTime;
    }

    public int getRefreshExpiredTime() {
        return refreshExpiredTime;
    }

    public char[] getPayloadSecretKeyChars() {
        return payloadSecretKey.toCharArray();
    }

    public byte[] getSaltBytes() {
        return salt.getBytes();
    }
}
