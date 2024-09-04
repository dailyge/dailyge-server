package project.dailyge.app.test.user.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.persistence.UserWriteDao;

@DisplayName("[UnitTest] UserDao 단위 테스트")
class UserDaoUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserWriteDao userWriteDao;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("JdbcTemplate이 DataAccessException을 던지면, DaoException이 발생한다.")
    void whenJdbcTemplateThrowsDataAccessExceptionThenDaoExceptionShouldBeThrown() {
        final DataAccessException dataAccessException = mock(DataAccessException.class);

        doThrow(dataAccessException).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        assertThrows(CommonException.class, () -> userWriteDao.save("test@gmail.com"));
    }
}
