package project.dailyge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.repository.config.*;

import java.time.*;

@Getter
@MappedSuperclass
@EnableJpaAuditing
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
