package project.dailyge.app.common.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor
public class JwtProperties {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expired-time}")
    private int accessExpiredTime;

    @Value("${jwt.refresh-expired-time}")
    private int refreshExpiredTime;

    public JwtProperties(
        final String secretKey,
        final int accessExpiredTime,
        final int refreshExpiredTime
    ) {
        this.secretKey = secretKey;
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
    }
}
