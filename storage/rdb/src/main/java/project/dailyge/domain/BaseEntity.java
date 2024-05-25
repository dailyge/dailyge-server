package project.dailyge.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.repository.config.*;

import java.time.*;

@MappedSuperclass
@EnableJpaAuditing
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last modified at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "delete")
    private Boolean deleted;
}
