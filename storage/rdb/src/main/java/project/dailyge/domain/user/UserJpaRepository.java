package project.dailyge.domain.user;

import org.springframework.data.jpa.repository.*;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

	UserJpaEntity findByEmail(final String email);
}
