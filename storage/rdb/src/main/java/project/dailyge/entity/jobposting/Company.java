package project.dailyge.entity.jobposting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

@Entity(name = "company")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size")
    private CompanySize companySize;

    protected Company() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CompanySize getCompanySize() {
        return companySize;
    }
}
