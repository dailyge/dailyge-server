package project.dailyge.app.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.configuration.web.JwtProperties;
import project.dailyge.domain.user.UserJpaEntity;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public DailygeToken createToken(UserJpaEntity user) {
        String accessToken = generateToken(user, jwtProperties.getAccessExpiredTime());
        String refreshToken = generateToken(user, jwtProperties.getRefreshExpiredTime());
        return new DailygeToken(accessToken, refreshToken, jwtProperties.getRefreshExpiredTime() * 3600);
    }

    private String generateToken(final UserJpaEntity user, final int expiredTime) {
        return makeToken(getExpiry(expiredTime), user);
    }

    private Date getExpiry(final int expiredTime) {
        Date now = new Date();
        return new Date(now.getTime() + Duration.ofHours(expiredTime).toMillis());
    }

    private String makeToken(
        final Date expiry,
        final UserJpaEntity user
    ) {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setExpiration(expiry)
            .setSubject(user.getEmail())
            .claim("id", user.getId())
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
            .compact();
    }

    public boolean isValidToken(final String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody();
    }
}
