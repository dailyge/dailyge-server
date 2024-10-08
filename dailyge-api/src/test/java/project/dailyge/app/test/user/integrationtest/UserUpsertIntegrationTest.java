package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.entity.user.UserJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;

@DisplayName("[IntegrationTest] 사용자 등록 및 갱신 통합 테스트")
class UserUpsertIntegrationTest extends DatabaseTestBase {

    private static final String TEST_URL = "testURL";

    @Autowired
    private UserWriteService userWriteService;

    @Test
    @DisplayName("이메일이 없다면, 새로 저장한다.")
    void whenUpsertUserByNonExistentThenUserShouldBeSave() {
        final UserJpaEntity userJpaEntity = userWriteService.upsert(createUser(3L, "dailyges", "other@gmail.com"));

        assertEquals(3L, userJpaEntity.getId());
    }

    @Test
    @DisplayName("사용자가 존재한다면, 정보를 업데이트 한다.")
    void whenUserExistsThenUserShouldBeUpdate() {
        final UserJpaEntity user = userWriteService.save(createUser(2L, "dailyges", "dailyges@gmail.com"));
        final UserJpaEntity newUser = new UserJpaEntity(2L, user.getNickname(), user.getEmail(), TEST_URL);

        final UserJpaEntity updateUser = userWriteService.upsert(newUser);

        assertEquals(user.getId(), updateUser.getId());
        assertEquals(TEST_URL, updateUser.getProfileImageUrl());
    }
}
