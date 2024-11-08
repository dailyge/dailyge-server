package project.dailyge.app.common.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.payload-secret-key}")
    private String payloadSecretKey;

    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.access-expired-time}")
    private int accessExpiredTime;

    @Value("${jwt.refresh-expired-time}")
    private int refreshExpiredTime;

    private JwtProperties() {
    }

    public JwtProperties(
        final String secretKey,
        final String payloadSecretKey,
        final String salt,
        final int accessExpiredTime,
        final int refreshExpiredTime
    ) {
        this.secretKey = secretKey;
        this.payloadSecretKey = payloadSecretKey;
        this.salt = salt;
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
    }

    public char[] getPayloadSecretKeyChars() {
        return payloadSecretKey.toCharArray();
    }

    public byte[] getSaltBytes() {
        return salt.getBytes();
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
}
