package project.dailyge.app.test.common;

import org.jasypt.encryption.StringEncryptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.configuration.jasypt.JasyptConfiguration;

@DisplayName("[UnitTest] Jasypt 단위 테스트")
class JasyptConfigurationUnitTest {

    private JasyptConfiguration jasyptConfiguration;

    @BeforeEach
    void setUp() {
        jasyptConfiguration = new JasyptConfiguration();
    }

    @Test
    @DisplayName("암호화를 한 후, 복호화를 하면 결과가 동일하다.")
    void whenDecryptAfterEncryptThenResultShouldBeSameOfOriginal() {
        String name = "Dailyge";
        StringEncryptor encryptor = jasyptConfiguration.stringEncryptor();
        String encryptName = encryptor.encrypt(name);

        assertEquals(name, encryptor.decrypt(encryptName));
    }
}
