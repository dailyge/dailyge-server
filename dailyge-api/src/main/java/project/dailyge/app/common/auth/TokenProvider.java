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
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.entity.user.UserJpaEntity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_PARAMETERS;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ID = "id";
    private static final String BEARER = "Bearer ";
    private static final int TOKEN_BEGIN_INDEX = 7;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int ARRAY_START_INDEX = 0;
    private static final int IV_SIZE = 16;
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
                .claim(ID, encryptUserId(user.getId()))
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
        if (claims == null) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
        }
        if (claims.get(ID) == null) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
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
        } catch (
            IllegalArgumentException |
            SignatureException |
            MalformedJwtException |
            UnsupportedJwtException |
            RequiredTypeException ex
        ) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    public String getAccessToken(final String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
        }
        if (!authorizationHeader.startsWith(BEARER)) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
        }
        return authorizationHeader.substring(TOKEN_BEGIN_INDEX);
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

            final byte[] encryptedUserIdWithIv = new byte[iv.length + encryptedUserId.length];
            System.arraycopy(iv, ARRAY_START_INDEX, encryptedUserIdWithIv, ARRAY_START_INDEX, iv.length);
            System.arraycopy(encryptedUserId, ARRAY_START_INDEX, encryptedUserIdWithIv, iv.length, encryptedUserId.length);

            return Base64.getEncoder().encodeToString(encryptedUserIdWithIv);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    public Long decryptUserId(final String encryptedUserIdWithIv) {
        try {
            final byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedUserIdWithIv);

            final byte[] iv = new byte[IV_SIZE];
            final byte[] encryptedUserId = new byte[encryptedWithIv.length - iv.length];
            System.arraycopy(encryptedWithIv, ARRAY_START_INDEX, iv, ARRAY_START_INDEX, iv.length);
            System.arraycopy(encryptedWithIv, iv.length, encryptedUserId, ARRAY_START_INDEX, encryptedUserId.length);

            final byte[] decrypted = applyCipher(iv, Cipher.DECRYPT_MODE, encryptedUserId);

            final String decryptId = new String(decrypted, StandardCharsets.UTF_8);
            return Long.parseLong(decryptId);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    private byte[] applyCipher(
        final byte[] iv,
        final int decryptMode,
        final byte[] cipherTarget
    ) {
        try {
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec spec = new PBEKeySpec(
                jwtProperties.getPayloadSecretKey().toCharArray(),
                jwtProperties.getSalt().getBytes(),
                65536,
                256
            );
            final SecretKey secretKey = factory.generateSecret(spec);
            final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(decryptMode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(cipherTarget);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }
}
