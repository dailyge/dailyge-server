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
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import project.dailyge.entity.BaseEntity;

@Entity(name = "job_posting")
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "required_skills")
    private String requiredSkills;

    @Column(name = "link")
    private String link;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "needed_documents", columnDefinition = "json")
    private List<String> neededDocuments;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobPostingStatus status;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

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

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public String getLink() {
        return link;
    }

    public List<String> getNeededDocuments() {
        return neededDocuments;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getUserId() {
        return userId;
    }
}
