package project.dailyge.app.common.configuration.web;

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

    @Value("${jwt.accessExpiredTime}")
    private int accessExpiredTime;

    @Value("${jwt.refreshExpiredTime}")
    private int refreshExpiredTime;

    public JwtProperties(final String secretKey, final int accessExpiredTime, final int refreshExpiredTime) {
        this.secretKey = secretKey;
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
    }
}
