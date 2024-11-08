package project.dailyge.entity.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;
import project.dailyge.entity.common.FileExtension;

@Entity(name = "event_image")
public class EventImageJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "size")
    private long size;

    @Column(name = "extension")
    @Enumerated(EnumType.STRING)
    private FileExtension extension;

    @Column(name = "event_id")
    private Long eventId;

    protected EventImageJpaEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }

    public FileExtension getExtension() {
        return extension;
    }

    public Long getEventId() {
        return eventId;
    }
}
