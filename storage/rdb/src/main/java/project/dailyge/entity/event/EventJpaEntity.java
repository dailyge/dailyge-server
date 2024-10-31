package project.dailyge.entity.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity(name = "events")
public class EventJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "coupon_quantity")
    private int couponQuantity;

    @Column(name = "coupon_issuance")
    private boolean couponIssuance = false;

    @Column(name = "participants")
    private long participants;

    protected EventJpaEntity() {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getCouponQuantity() {
        return couponQuantity;
    }

    public boolean isCouponIssuance() {
        return couponIssuance;
    }

    public long getParticipants() {
        return participants;
    }
}
