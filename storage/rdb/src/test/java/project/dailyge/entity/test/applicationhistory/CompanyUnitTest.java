package project.dailyge.entity.test.applicationhistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.applicationhistory.CompanyJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.entity.applicationhistory.CompanyType.STARTUP;

@DisplayName("[UnitTest] Company 단위 테스트")
public class CompanyUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 회사가 생성된다.")
    void whenValidArgumentsProvidedThenCompanyIsCreated() {
        final CompanyJpaEntity company = new CompanyJpaEntity("Dailyge", STARTUP);

        assertAll(
            () -> assertEquals("Dailyge", company.getName()),
            () -> assertEquals(STARTUP, company.getCompanyType())
        );
    }
}
