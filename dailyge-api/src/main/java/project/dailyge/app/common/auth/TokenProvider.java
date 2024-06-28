package project.dailyge.app.common.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.configuration.web.JwtProperties;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.Duration;
import java.util.Date;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.common.exception.UnAuthorizedException.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ID = "id";
    private static final String BEARER = "Bearer ";
    private static final int TOKEN_BEGIN_INDEX = 7;

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
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
            .compact();
    }

    public void validateToken(final String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
        }catch (IllegalArgumentException e) {
            throw new UnAuthorizedException(EMPTY_TOKEN_ERROR_MESSAGE, UN_AUTHORIZED);
        } catch (SignatureException e) {
            throw new UnAuthorizedException(TOKEN_SIGNATURE_VERIFICATION_FAILED_ERROR_MESSAGE, UN_AUTHORIZED);
        } catch (MalformedJwtException e) {
            throw new UnAuthorizedException(TOKEN_FORMAT_INCORRECT_ERROR_MESSAGE, UN_AUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException(EXPIRED_TOKEN_ERROR_MESSAGE, UN_AUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new UnAuthorizedException(UNSUPPORTED_TOKEN_ERROR_MESSAGE, UN_AUTHORIZED);
        }  catch (Throwable e) {
            log.error(e.getMessage());
        }
    }

    public Long getUserId(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null || claims.get(ID) == null) {
            throw new UnAuthorizedException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        try {
            return claims.get(ID, Long.class);
        } catch (RequiredTypeException ex) {
            throw new UnAuthorizedException(INVALID_ID_TYPE_MESSAGE, INVALID_USER_TOKEN);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
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

    public String getAccessToken(final String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            return authorizationHeader.substring(TOKEN_BEGIN_INDEX);
        }
        return authorizationHeader;
    }
}
