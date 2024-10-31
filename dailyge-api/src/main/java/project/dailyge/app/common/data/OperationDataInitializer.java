package project.dailyge.app.common.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile({"local", "dev"})
public class OperationDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OperationDataInitializer.class);

    private static final String LOCAL = "local";
    private static final String DEV = "dev";
    private static final String PROD = "prod";

    @Value("${env}")
    private String env;

    private final OperationDataWriteDao operationWriteDao;

    public OperationDataInitializer(final OperationDataWriteDao operationWriteDao) {
        this.operationWriteDao = operationWriteDao;
    }

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
