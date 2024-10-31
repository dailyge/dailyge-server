package project.dailyge.app.test.common;

import org.jasypt.encryption.StringEncryptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.configuration.jasypt.DailygeJasyptConfig;

@DisplayName("[UnitTest] Jasypt 단위 테스트")
class DailygeJasyptConfigUnitTest {

    private DailygeJasyptConfig dailygeJasyptConfig;

    @BeforeEach
    void setUp() {
        dailygeJasyptConfig = new DailygeJasyptConfig();
    }

    @Test
    @DisplayName("암호화를 한 후, 복호화를 하면 결과가 동일하다.")
    void whenDecryptAfterEncryptThenResultShouldBeSameOfOriginal() {
        final String name = "Dailyge";
        final StringEncryptor encryptor = dailygeJasyptConfig.stringEncryptor();
        final String encryptName = encryptor.encrypt(name);

        assertEquals(name, encryptor.decrypt(encryptName));
    }
}
