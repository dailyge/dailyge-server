package project.dailyge.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
