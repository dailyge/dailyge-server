package project.dailyge.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;
import project.dailyge.entity.BaseEntity;

@Entity(name = "users")
public class UserJpaEntity extends BaseEntity {

    private static final int MAX_NICKNAME_LENGTH = 24;
    private static final int MAX_PROFILE_IMAGE_URL_LENGTH = 2000;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[0-9a-zA-Z]+(?:[._-]?[0-9a-zA-Z]+)*@gmail\\.com$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣._-][a-zA-Z0-9가-힣._\\s-]{2,24}$");
    private static final String OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE = "입력 가능한 최대 닉네임 길이를 초과했습니다.";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "유효하지 않는 이메일 형식입니다.";
    private static final String OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE = "입력 가능한 최대 URL 길이를 초과했습니다.";
    private static final String USER_ALREADY_DELETED_MESSAGE = "이미 탈퇴한 사용자입니다.";

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

    @Column(name = "is_blacklist")
    private boolean isBlacklist;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected UserJpaEntity() {
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email,
        final boolean isBlacklist
    ) {
        validate(nickname, email, "");
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.NORMAL;
        this.isBlacklist = isBlacklist;
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email
    ) {
        this(id, nickname, email, false);
    }

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email,
        final Role role
    ) {
        validate(nickname, email, "");
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.isBlacklist = false;
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
        this.isBlacklist = false;
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
        validateNickname(nickname);
        validateEmail(email);
        validateProfile(profileImageUrl);
    }

    public void updateNickname(final String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(final String nickname) {
        if (MAX_NICKNAME_LENGTH < nickname.length()) {
            throw new IllegalArgumentException(OVER_MAX_NICKNAME_LENGTH_ERROR_MESSAGE);
        }
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException("올바른 닉네임을 입력해주세요.");
        }
    }

    private void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL_ERROR_MESSAGE);
        }
    }

    private void validateProfile(final String profileImageUrl) {
        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            return;
        }
        if (MAX_PROFILE_IMAGE_URL_LENGTH < profileImageUrl.length()) {
            throw new IllegalArgumentException(OVER_MAX_PROFILE_IMAGE_URL_ERROR_MESSAGE);
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

    public boolean isBlacklist() {
        return isBlacklist;
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
