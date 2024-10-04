package project.dailyge.entity.retrospect;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import project.dailyge.entity.BaseEntity;
import static jakarta.persistence.EnumType.STRING;

public class Retrospect extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(STRING)
    @Column(name = "retrospect_type")
    private RetrospectType retrospectType;

    @Column(name = "retrospect_date")
    private LocalDate retrospectDate;

    @Column(name = "user_id")
    private Long userId;
}
