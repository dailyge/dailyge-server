package project.dailyge.document;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
public abstract class DocumentBaseEntity {

    @CreatedDate
    @Field(name = "created_at")
    protected LocalDateTime createdAt;

    @Field(name = "created_by")
    protected Long createdBy;

    @LastModifiedDate
    @Field(name = "last_modified_at")
    protected LocalDateTime lastModifiedAt;

    @Field(name = "last_modified_by")
    protected Long lastModifiedBy;

    @Field(name = "deleted")
    protected boolean deleted = false;

    public void initOperatingColumns(final Long userId) {
        final LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.createdBy = userId;
        this.lastModifiedAt = now;
        this.lastModifiedBy = userId;
    }
}
