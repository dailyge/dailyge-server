package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;

@DisplayName("[IntegrationTest] 사용자 등록 및 갱신 통합 테스트")
public class UserUpsertIntegrationTest extends DatabaseTestBase {

    private static final String TEST_URL = "testURL";

    @Autowired
    private UserReadUseCase userReadUseCase;
    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자가 없다면, 새로 저장한다.")
    void whenUpsertUserByNonExistentThenUserShouldBeSave() {
        final UserJpaEntity newUser = createUserJpaEntity();
        final Optional<UserJpaEntity> findByEmailUser = userReadUseCase.findActiveUserByEmail(newUser.getEmail());

        final UserJpaEntity userJpaEntity = userWriteUseCase.upsert(createUserJpaEntity());

        assertFalse(findByEmailUser.isPresent());
        assertNotNull(userJpaEntity.getId());
    }

    @Test
    @DisplayName("사용자가 존재한다면, 정보를 업데이트 한다.")
    void whenThen() {
        final UserJpaEntity user = userWriteUseCase.save(createUserJpaEntity());
        final UserJpaEntity newUser = new UserJpaEntity(user.getNickname(), user.getEmail(), TEST_URL);

        final UserJpaEntity updateUser = userWriteUseCase.upsert(newUser);

        assertEquals(user.getId(), updateUser.getId());
        assertEquals(updateUser.getProfileImageUrl(), TEST_URL);
    }
}
