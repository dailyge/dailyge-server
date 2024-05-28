package project.dailyge.domain.user;

import jakarta.persistence.*;
import lombok.*;
import project.dailyge.domain.BaseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public UserJpaEntity(
        final Long id,
        final String nickname,
        final String email
    ) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
