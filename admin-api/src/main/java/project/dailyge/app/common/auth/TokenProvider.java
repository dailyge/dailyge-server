package project.dailyge.app.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ID = "id";
    private static final int ARRAY_START_INDEX = 0;
    private static final int IV_SIZE = 12;
    private static final SecureRandom secureRandom = new SecureRandom();

    private final JwtProperties jwtProperties;
    private final SecretKeyManager secretKeyManager;

    public DailygeToken createToken(final Long userId) {
        final String accessToken = generateToken(userId, getExpiry(jwtProperties.getAccessExpiredTime()));
        final String refreshToken = generateToken(userId, getExpiry(jwtProperties.getRefreshExpiredTime()));
        return new DailygeToken(accessToken, refreshToken, jwtProperties.getAccessExpiredTime(), jwtProperties.getRefreshExpiredTime());
    }

    private Date getExpiry(final int expiredTime) {
        final Date now = new Date();
        return new Date(now.getTime() + Duration.ofDays(expiredTime).toMillis());
    }

    private String generateToken(
        final Long userId,
        final Date expiry
    ) {
        try {
            return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(expiry)
                .claim(ID, encryptUserId(userId))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
        } catch (IllegalArgumentException ex) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), UN_AUTHORIZED);
        }
    }

    public Long getUserId(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null) {
            throw CommonException.from(INVALID_USER_TOKEN);
        }
        if (claims.get(ID) == null) {
            throw CommonException.from(INVALID_USER_TOKEN);
        }
        final String encryptUserId = claims.get(ID, String.class);
        return decryptUserId(encryptUserId);
    }

    private Claims getClaims(final String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        } catch (IllegalArgumentException
                 | SignatureException
                 | MalformedJwtException
                 | UnsupportedJwtException
                 | RequiredTypeException ex
        ) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    public String encryptUserId(final Long userId) {
        try {
            final byte[] iv = new byte[IV_SIZE];
            secureRandom.nextBytes(iv);
            final byte[] encryptedUserId = applyCipher(
                iv,
                Cipher.ENCRYPT_MODE,
                userId.toString().getBytes(StandardCharsets.UTF_8)
            );

            final byte[] encryptedUserIdWithIv = new byte[IV_SIZE + encryptedUserId.length];
            System.arraycopy(iv, ARRAY_START_INDEX, encryptedUserIdWithIv, ARRAY_START_INDEX, IV_SIZE);
            System.arraycopy(encryptedUserId, ARRAY_START_INDEX, encryptedUserIdWithIv, IV_SIZE, encryptedUserId.length);

            return Base64.getEncoder().encodeToString(encryptedUserIdWithIv);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    public Long decryptUserId(final String encryptedUserIdWithIv) {
        try {
            final byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedUserIdWithIv);

            final byte[] iv = new byte[IV_SIZE];
            final byte[] encryptedUserId = new byte[encryptedWithIv.length - IV_SIZE];
            System.arraycopy(encryptedWithIv, ARRAY_START_INDEX, iv, ARRAY_START_INDEX, IV_SIZE);
            System.arraycopy(encryptedWithIv, IV_SIZE, encryptedUserId, ARRAY_START_INDEX, encryptedUserId.length);

            final byte[] decrypted = applyCipher(iv, Cipher.DECRYPT_MODE, encryptedUserId);

            final String decryptId = new String(decrypted, StandardCharsets.UTF_8);
            return Long.parseLong(decryptId);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    private byte[] applyCipher(
        final byte[] iv,
        final int operationMode,
        final byte[] cipherTarget
    ) {
        try {
            final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            final SecretKeySpec secretKeySpec = secretKeyManager.getSecretKeySpec();
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(operationMode, secretKeySpec, gcmParameterSpec);
            return cipher.doFinal(cipherTarget);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }
}
