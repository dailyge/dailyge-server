package project.dailyge.app.test.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.entity.user.Role;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[UnitTest] DailygeUser 단위 테스트")
class DailygeUserTest {

    @Test
    @DisplayName("권한 체크 시 관리자일 경우, True를 반환한다.")
    void whenUserRoleIsAdminThenResultShouldBeTrue() {
        final DailygeUser dailygeUser = new DailygeUser(1L, Role.ADMIN);

        assertTrue(dailygeUser.isAdmin());
    }

    @Test
    @DisplayName("권한 체크 시 관리자가 아닐 경우, False를 반환한다. ")
    void whenUserRoleIsNotAdminThenReturnShouldBeFalse() {
        final DailygeUser dailygeUser = new DailygeUser(1L, Role.NORMAL);

        assertFalse(dailygeUser.isAdmin());
    }
}
