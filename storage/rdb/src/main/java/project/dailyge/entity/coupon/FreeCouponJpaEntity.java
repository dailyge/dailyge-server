package project.dailyge.entity.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "free_coupons")
public class FreeCouponJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "expired_at")
    private LocalDate expiredAt;

    @Column(name = "issuer_id")
    private Long issuerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;

    protected FreeCouponJpaEntity() {
    }

    public FreeCouponJpaEntity(
        final Long id,
        final String description,
        final String serialNumber,
        final LocalDate expiredAt,
        final Long issuerId,
        final Long userId,
        final Long eventId
    ) {
        this.id = id;
        this.description = description;
        this.serialNumber = serialNumber;
        this.expiredAt = expiredAt;
        this.issuerId = issuerId;
        this.userId = userId;
        this.eventId = eventId;
    }

    public FreeCouponJpaEntity(
        final String description,
        final String serialNumber,
        final LocalDate expiredAt,
        final Long issuerId,
        final Long userId,
        final Long eventId
    ) {
        this(null, description, serialNumber, expiredAt, issuerId, userId, eventId);
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public LocalDate getExpiredAt() {
        return expiredAt;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof FreeCouponJpaEntity that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
