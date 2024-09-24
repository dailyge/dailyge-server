package project.dailyge.app.common.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class OperationDataInitializer implements CommandLineRunner {

    private static final String LOCAL = "local";
    private static final String DEV = "dev";
    private static final String PROD = "prod";

    @Value("${env}")
    private String env;

    private final OperationDataWriteDao operationWriteDao;

    @Override
    @Transactional
    public void run(final String... args) {
        if (isValidEnv(env)) {
            clearData();
            initData();
            log.info("Data initialized success.");
        }
    }

    private void clearData() {
        if (isProd()) {
            return;
        }
        operationWriteDao.clearData();
    }

    private void initData() {
        operationWriteDao.initData();
    }

    private boolean isProd() {
        return PROD.equals(env);
    }

    private boolean isValidEnv(final String env) {
        return LOCAL.equals(env)
            || DEV.equals(env);
    }
}
