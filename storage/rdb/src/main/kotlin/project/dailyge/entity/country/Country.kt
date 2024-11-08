package project.dailyge.entity.country

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity

@Entity(name = "countries")
class Country(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "kr_name")
    val krName: String,

    @Column(name = "en_name")
    val enName: String,

    @Column(name = "code")
    val code: String,

    @Column(name = "alpha2")
    val alpha2: String,

    @Column(name = "alpha3")
    val alpha3: String,
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Country) {
            return false
        }
        return code == other.code
    }

    override fun hashCode(): Int = code.hashCode()
}
