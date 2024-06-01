package project.dailyge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @Column(name = "created_by")
    protected Long createdBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    protected LocalDateTime lastModifiedAt;

    @Column(name = "last_modified_by")
    protected Long lastModifiedBy;

    @Column(name = "deleted")
    protected Boolean deleted = false;
}
