package project.dailyge.entity.applicationhistory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

@Entity(name = "companies")
public class CompanyJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type")
    private CompanyType companyType;

    protected CompanyJpaEntity() {
    }

    public CompanyJpaEntity(
        final String name,
        final CompanyType companyType
    ) {
        this.name = name;
        this.companyType = companyType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }
}
