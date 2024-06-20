package project.dailyge.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.Boolean.TRUE;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class UserJpaEntity extends BaseEntity {

    private static final int MAX_NICKNAME_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final int MAX_PROFILE_IMAGE_URL_LENGTH = 2000;
    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@gmail\\.com$";
    private static final  String OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE = "입력 가능한 닉네임 길이를 초과했습니다.";
    private static final String OVER_MAX_EMAIL_LENGTH_ERROR_MESSAGE = "입력 가능한 이메일 길이를 초과했습니다.";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "유효하지 않는 이메일 형식입니다.";
    private static final String OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE = "입력 가능한 프로필 사진 URL 길이를 초과했습니다.";
    private static final String USER_ALREADY_DELETED_MESSAGE = "이미 탈퇴한 유저입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public UserJpaEntity(
        final Long userId,
        final String nickname,
        final String email
    ) {
        validate(nickname, email);
        this.id = userId;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
    }

    public UserJpaEntity(
        final Long userId,
        final String nickname,
        final String email,
        final LocalDateTime createdAt
    ) {
        validate(nickname, email);
        this.id = userId;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
        this.createdAt = createdAt;
    }

    public UserJpaEntity(
        final String nickname,
        final String email
    ) {
        validate(nickname, email);
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
    }

    public UserJpaEntity(
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        validate(nickname, email, profileImageUrl);
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.NORMAL;
    }

    private void validate(
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        if (MAX_PROFILE_IMAGE_URL_LENGTH < profileImageUrl.length()) {
            throw new IllegalArgumentException(OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE);
        }
        validate(nickname, email);
    }

    private void validate(
        final String nickname,
        final String email
    ) {
        if (MAX_NICKNAME_LENGTH < nickname.length()) {
            throw new IllegalArgumentException(OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE);
        }
        if (MAX_EMAIL_LENGTH < email.length()) {
            throw new IllegalArgumentException(OVER_MAX_EMAIL_LENGTH_ERROR_MESSAGE);
        }
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException(INVALID_EMAIL_ERROR_MESSAGE);
        }
    }

    public void delete() {
        if (deleted) {
            throw new IllegalArgumentException(USER_ALREADY_DELETED_MESSAGE);
        }
        this.deleted = TRUE;
        this.deletedAt = LocalDateTime.now();
    }

    public void profileImageInit(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static String getOverMaxNicknameLengthErrorMessage() {
        return OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE;
    }

    public static String getOverMaxEmailLengthErrorMessage() {
        return OVER_MAX_EMAIL_LENGTH_ERROR_MESSAGE;
    }

    public static String getInvalidEmailErrorMessage() {
        return INVALID_EMAIL_ERROR_MESSAGE;
    }

    public static String getOverMaxProfileImageUrlErrorMessage() {
        return OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE;
    }

    public static String getUserAlreadyDeletedMessage() {
        return USER_ALREADY_DELETED_MESSAGE;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJpaEntity user = (UserJpaEntity) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
