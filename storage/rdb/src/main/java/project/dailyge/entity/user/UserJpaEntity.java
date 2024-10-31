package project.dailyge.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity(name = "users")
public class UserJpaEntity extends BaseEntity {

    private static final int MAX_NICKNAME_LENGTH = 20;
    private static final int MAX_PROFILE_IMAGE_URL_LENGTH = 2000;
    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z](?:[-_.]?[0-9a-zA-Z]){0,39}@gmail\\.com$";
    private static final String OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE = "입력 가능한 최대 닉네임 길이를 초과했습니다.";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "유효하지 않는 이메일 형식입니다.";
    private static final String OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE = "입력 가능한 최대 URL 길이를 초과했습니다.";
    private static final String USER_ALREADY_DELETED_MESSAGE = "이미 탈퇴한 사용자입니다.";

    @Id
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

    protected UserJpaEntity() {
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email
    ) {
        validate(nickname, email);
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email,
        final LocalDateTime createdAt
    ) {
        validate(nickname, email);
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
        init(createdAt, id, null, null, false);
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email,
        final Role role
    ) {
        validate(nickname, email);
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        validate(nickname, email, profileImageUrl);
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.NORMAL;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
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
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException(INVALID_EMAIL_ERROR_MESSAGE);
        }
    }

    public void delete() {
        if (super.getDeleted()) {
            throw new IllegalArgumentException(USER_ALREADY_DELETED_MESSAGE);
        }
        updateDeletedStatus();
        this.deletedAt = LocalDateTime.now();
    }

    public String getRoleAsString() {
        return role.name();
    }

    public void profileImageInit(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getOverMaxNicknameLengthErrorMessage() {
        return OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE;
    }

    public String getInvalidEmailErrorMessage() {
        return INVALID_EMAIL_ERROR_MESSAGE;
    }

    public String getOverMaxProfileImageUrlErrorMessage() {
        return OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE;
    }

    public String getUserAlreadyDeletedMessage() {
        return USER_ALREADY_DELETED_MESSAGE;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserJpaEntity user = (UserJpaEntity) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
