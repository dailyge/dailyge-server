package project.dailyge.domain.user;

import org.springframework.data.jpa.repository.*;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
