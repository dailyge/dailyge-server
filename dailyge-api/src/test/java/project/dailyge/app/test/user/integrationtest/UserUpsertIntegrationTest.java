package project.dailyge.app.test.user.integrationtest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteService;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.entity.user.UserJpaEntity;

@DisplayName("[IntegrationTest] 사용자 등록 및 갱신 통합 테스트")
class UserUpsertIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteService userWriteService;

    @Test
    @DisplayName("중복된 Eamil이 없다면, 해당 Eamil로 새로운 사용자를 저장한다.")
    void whenUpsertUserByNonExistentThenUserShouldBeSave() {
        final UserJpaEntity userJpaEntity = userWriteService.upsert(createUser(null, "dailyges", "other@gmail.com"));
        assertNotNull(userJpaEntity.getId());
    }
}
