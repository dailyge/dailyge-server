package project.dailyge.app.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_PARAMETERS;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import project.dailyge.app.common.exception.JWTAuthenticationException;
import static project.dailyge.app.common.exception.JWTAuthenticationException.EMPTY_TOKEN_ERROR_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.INVALID_ID_TYPE_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.INVALID_TOKEN_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.TOKEN_FORMAT_INCORRECT_ERROR_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.TOKEN_SIGNATURE_VERIFICATION_FAILED_ERROR_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.UNSUPPORTED_TOKEN_ERROR_MESSAGE;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.Duration;
import java.util.Date;

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
        try {
            return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim(ID, user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
        } catch (IllegalArgumentException ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_PARAMETERS);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    public Long getUserId(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null || claims.get(ID) == null) {
            throw new JWTAuthenticationException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        return claims.get(ID, Long.class);
    }

    private Claims getClaims(final String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        } catch (IllegalArgumentException ex) {
            throw new JWTAuthenticationException(EMPTY_TOKEN_ERROR_MESSAGE, INVALID_USER_TOKEN);
        } catch (SignatureException ex) {
            throw new JWTAuthenticationException(TOKEN_SIGNATURE_VERIFICATION_FAILED_ERROR_MESSAGE, INVALID_USER_TOKEN);
        } catch (MalformedJwtException ex) {
            throw new JWTAuthenticationException(TOKEN_FORMAT_INCORRECT_ERROR_MESSAGE, INVALID_USER_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new JWTAuthenticationException(UNSUPPORTED_TOKEN_ERROR_MESSAGE, INVALID_USER_TOKEN);
        } catch (RequiredTypeException ex) {
            throw new JWTAuthenticationException(INVALID_ID_TYPE_MESSAGE, INVALID_USER_TOKEN);
        }
    }

    public String getAccessToken(final String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new JWTAuthenticationException(INVALID_TOKEN_MESSAGE, INVALID_USER_TOKEN);
        }
        return authorizationHeader.substring(TOKEN_BEGIN_INDEX);
    }
}
