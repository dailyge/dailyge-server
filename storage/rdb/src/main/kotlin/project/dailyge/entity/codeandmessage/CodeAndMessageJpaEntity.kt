package project.dailyge.entity.codeandmessage

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import project.dailyge.entity.BaseEntity

@Entity(name = "code_and_message")
class CodeAndMessageJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "domain")
    private var _domain: String,

    @Column(name = "name")
    private var _name: String,

    @Column(name = "code")
    private var _code: Int,

    @Column(name = "message")
    private var _message: String,
) : BaseEntity() {

    companion object {
        fun create(
            domain: String,
            name: String,
            code: Int,
            message: String,
        ): CodeAndMessageJpaEntity {
            return CodeAndMessageJpaEntity(_domain = domain, _name = name, _code = code, _message = message)
        }
    }

    val domain: String
        get() = _domain

    val name: String
        get() = _name

    val code: Int
        get() = _code

    val message: String
        get() = _message

    fun update(target: CodeAndMessageJpaEntity) {
        _domain = target.domain
        _name = target.name
        _code = target.code
        _message = target.message
    }
}
