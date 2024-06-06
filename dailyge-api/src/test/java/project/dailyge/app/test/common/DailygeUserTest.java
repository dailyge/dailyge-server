package project.dailyge.app.test.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.domain.user.Role;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[UnitTest] DailygeUser 단위 테스트")
class DailygeUserTest {

    @Test
    @DisplayName("권한 체크 시 어드민 일 경우, True를 반환한다.")
    void whenCheckAdminThenShouldBeTrue() {
        DailygeUser dailygeUser = new DailygeUser(1L, Role.ADMIN);

        assertTrue(dailygeUser.isAdmin());
    }

    @Test
    @DisplayName("권한 체크 시 어드민이 아닐 경우, False를 반환한다. ")
    void whenCheckNotAdminThenShouldBeFalse() {
        DailygeUser dailygeUser = new DailygeUser(1L, Role.NORMAL);

        assertFalse(dailygeUser.isAdmin());
    }

    @Test
    @DisplayName("로그인 정보와 동일한 사용자 ID를 비교할 경우, 에러가 발생하지 않는다.")
    void whenCheckUserThenShouldBePass() {
        DailygeUser dailygeUser = new DailygeUser(1L, Role.NORMAL);

        assertDoesNotThrow(dailygeUser::isAdmin);
    }

    @Test
    @DisplayName("로그인 정보와 다른 사용자 ID를 비교할 경우, UnAuthorizedException이 발생한다.")
    void whenCheckOtherUserThenShouldBeUnAuthorizedException() {
        DailygeUser dailygeUser = new DailygeUser(1L, Role.NORMAL);

        assertFalse(dailygeUser.isAdmin());
    }
}
