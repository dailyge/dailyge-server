package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

@Entity(name = "task_labels")
public class TaskLabelJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "user_id")
    private Long userId;

    protected TaskLabelJpaEntity() {
    }

    public TaskLabelJpaEntity(
        final String name,
        final String description,
        final String color,
        final Long userId
    ) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public Long getUserId() {
        return userId;
    }
}
