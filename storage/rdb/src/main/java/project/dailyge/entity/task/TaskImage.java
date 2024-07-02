package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import project.dailyge.entity.BaseEntity;

@Getter
@Entity(name = "task_images")
public class TaskImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size")
    private long size;

    @Column(name = "url")
    private String url;

    @Column(name = "extension")
    @Enumerated(EnumType.STRING)
    private TaskFileExtension extension;

    @Column(name = "task_id")
    private Long taskId;
}
