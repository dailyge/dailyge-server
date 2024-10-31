package project.dailyge.entity.holiday

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity
import java.time.LocalDate

@Entity(name = "holidays")
class HolidayJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    private var _name: String,

    @Column(name = "date")
    private var _date: LocalDate,

    @Column(name = "holiday")
    val holiday: Boolean,

    @Column(name = "country_id")
    val countryId: Long,
) : BaseEntity() {

    val name: String
        get() = _name

    val date: LocalDate
        get() = _date

    val dateAsString: String
        get() = _date.toString()
}
