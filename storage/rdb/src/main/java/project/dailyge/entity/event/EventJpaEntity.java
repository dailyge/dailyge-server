package project.dailyge.entity.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity(name = "events")
@NoArgsConstructor(access = PROTECTED)
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

    public EventJpaEntity(
        final String name,
        final String description,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final int couponQuantity,
        final boolean couponIssuance,
        final long participants
    ) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.couponQuantity = couponQuantity;
        this.couponIssuance = couponIssuance;
        this.participants = participants;
    }
}
