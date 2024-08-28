package project.dailyge.app.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.UnAuthorizedException;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ID = "id";
    private static final int ARRAY_START_INDEX = 0;
    private static final int IV_SIZE = 16;

    private final JwtProperties jwtProperties;
    private final SecretKeyManager secretKeyManager;

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
        } catch (IllegalArgumentException
                 | SignatureException
                 | MalformedJwtException
                 | UnsupportedJwtException
                 | RequiredTypeException ex
        ) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    public String getAccessToken(final String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new UnAuthorizedException(INVALID_USER_TOKEN);
        }
        return authorizationHeader.substring(7);
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
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }

    private byte[] applyCipher(
        final byte[] iv,
        final int operationMode,
        final byte[] cipherTarget
    ) {
        try {
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            final SecretKeySpec secretKeySpec = secretKeyManager.getSecretKeySpec();
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(operationMode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(cipherTarget);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        }
    }
}
