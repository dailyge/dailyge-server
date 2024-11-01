package project.dailyge.entity.jobposting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import project.dailyge.entity.BaseEntity;

@Entity(name = "job_posting")
public class ResumeSubmissionHistoryJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public ProgressStatus getStatus() {
        return status;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getUserId() {
        return userId;
    }
}
