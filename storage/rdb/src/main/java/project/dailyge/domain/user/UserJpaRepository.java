package project.dailyge.domain.user;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

	Optional<UserJpaEntity> findByEmail(final String email);
}
