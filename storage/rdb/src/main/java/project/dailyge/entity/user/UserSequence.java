package project.dailyge.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "user_sequence")
public class UserSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "executed")
    private Boolean executed;

    protected UserSequence() {
        this.executed = false;
    }

    public UserSequence(
        final Long id,
        final Boolean executed
    ) {
        this.id = id;
        this.executed = executed;
    }

    public static UserSequence createNewSequence() {
        return new UserSequence();
    }

    public void changeExecuted() {
        if (!executed) {
            this.executed = true;
        }
    }
}
