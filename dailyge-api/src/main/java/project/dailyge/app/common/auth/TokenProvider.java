package project.dailyge.app.common.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.configuration.web.JwtProperties;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.domain.user.Role;
import project.dailyge.domain.user.UserJpaEntity;

import java.time.Duration;
import java.util.Date;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.common.exception.UnAuthorizedException.INVALID_TOKEN_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ID = "id";
    private static final String NICKNAME = "nickname";
    private static final String ROLE = "role";

    private final JwtProperties jwtProperties;

    public DailygeToken createToken(final UserJpaEntity user) {
        final String accessToken = generateToken(user, getExpiry(jwtProperties.getAccessExpiredTime()));
        final String refreshToken = generateToken(user, getExpiry(jwtProperties.getRefreshExpiredTime()));
        return new DailygeToken(accessToken, refreshToken, jwtProperties.getRefreshExpiredTime() * 3600);
    }

    private Date getExpiry(final int expiredTime) {
        final Date now = new Date();
        return new Date(now.getTime() + Duration.ofHours(expiredTime).toMillis());
    }

    private String generateToken(
        final UserJpaEntity user,
        final Date expiry
    ) {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setExpiration(expiry)
            .setSubject(user.getEmail())
            .claim(ID, user.getId())
            .claim(NICKNAME, user.getNickname())
            .claim(ROLE, user.getRole().toString())
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
            .compact();
    }

    public boolean isValidToken(final String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("서명 검증에 실패하였습니다.: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT 형식이 올바르지 않습니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT 만료 되었습니다.: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 입니다.: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("비어있는 JWT 입니다.: {}", e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public Long getUserId(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null || claims.get(ID) == null) {
            throw new UnAuthorizedException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        return claims.get(ID, Long.class);
    }

    public String getUserNickname(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null || claims.get(NICKNAME) == null) {
            throw new UnAuthorizedException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        return claims.get(NICKNAME, String.class);
    }

    public String getUserRole(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null || claims.get(ROLE) == null) {
            throw new UnAuthorizedException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        return claims.get(ROLE, String.class);
    }

    private Claims getClaims(final String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        } catch (Throwable e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
