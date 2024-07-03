package project.dailyge.entity.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDate;

@Getter
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
}
