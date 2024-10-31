package project.dailyge.app.common.configuration.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.dailyge.security.jasyp.JasyptConfig;

@Configuration
@EnableEncryptableProperties
public class DailygeJasyptConfig {

    private final String password = System.getenv("ENCRYPTOR_KEY");

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("비밀키를 찾을 수 없습니다.");
        }
        final JasyptConfig config = new JasyptConfig(password);
        return config.createStringEncryptor();
    }
}
