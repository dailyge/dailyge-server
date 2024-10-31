package project.dailyge.app.test.common;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.configuration.jasypt.AdminJasyptConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] Jasypt 단위 테스트")
class AdminJasyptConfigurationUnitTest {

    private AdminJasyptConfiguration adminJasyptConfiguration;

    @BeforeEach
    void setUp() {
        adminJasyptConfiguration = new AdminJasyptConfiguration();
    }

    @Test
    @DisplayName("암호화를 한 후, 복호화를 하면 결과가 동일하다.")
    void whenDecryptAfterEncryptThenResultShouldBeSameOfOriginal() {
        final String name = "Dailyge";
        final StringEncryptor encryptor = adminJasyptConfiguration.stringEncryptor();
        final String encryptName = encryptor.encrypt(name);

        assertEquals(name, encryptor.decrypt(encryptName));
    }
}
