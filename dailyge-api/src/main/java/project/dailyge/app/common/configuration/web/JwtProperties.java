package project.dailyge.app.common.configuration.web;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.accessExpiredTime}")
    private int accessExpiredTime;

    @Value("${jwt.refreshExpiredTime}")
    private int refreshExpiredTime;
}
