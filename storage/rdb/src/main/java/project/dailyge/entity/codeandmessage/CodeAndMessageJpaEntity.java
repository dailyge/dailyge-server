package project.dailyge.entity.codeandmessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import project.dailyge.entity.BaseEntity;

@Getter
@Entity(name = "code_and_message")
public class CodeAndMessageJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private int code;

    @Column(name = "message")
    private String message;

    protected CodeAndMessageJpaEntity() {
    }

    private CodeAndMessageJpaEntity(
        final String domain,
        final String name,
        final int code,
        final String message
    ) {
        this.domain = domain;
        this.name = name;
        this.code = code;
        this.message = message;
    }

    @Builder
    public static CodeAndMessageJpaEntity create(
        final String domain,
        final String name,
        final int code,
        final String message
    ) {
        return new CodeAndMessageJpaEntity(domain, name, code, message);
    }

    public void update(final CodeAndMessageJpaEntity target) {
        this.domain = target.getDomain();
        this.name = target.getName();
        this.code = target.getCode();
        this.message = target.getMessage();
    }
}
