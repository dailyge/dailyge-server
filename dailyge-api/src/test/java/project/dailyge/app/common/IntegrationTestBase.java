package project.dailyge.app.common;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public abstract class IntegrationTestBase extends DatabaseTestBase {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @BeforeEach
    void setUp() {
        databaseInitializer.truncateAllEntity();
    }
}
