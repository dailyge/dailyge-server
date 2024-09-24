package project.dailyge.app.core.common.web;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.presentation.CouponReadApi;
import project.dailyge.app.core.event.presentation.EventCreateApi;
import static project.dailyge.entity.user.Role.NORMAL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerWarmUpRunner implements ApplicationRunner {

    private static final int START = 1;
    private static final int END = 10;

    private final CouponReadApi couponReadApi;
    private final EventCreateApi eventCreateApi;
    private final RestTemplate restTemplate;
    private final EntityManager entityManager;
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public void run(final ApplicationArguments args) {
        try {
            warmUp("Database Selects", this::warmUpRdb);
            warmUp("Redis Pongs", this::warmUpRedis);
            warmUp("External API Call", this::warmUpExternalApiCaller);
            warmUp("CouponRead Method Warm Up", this::warmUpCouponReadApi);
            warmUp("Event Participate Method Warm Up", this::warmUpEventParticipantApi);
        } catch (Exception ex) {
            log.error("WarmUp Ex: {}", ex.getMessage());
        }
    }

    private <T> void warmUp(
        final String taskName,
        final Supplier<T> execution
    ) {
        final List<T> results = execute(execution);
        log.info("{} Size: {}", taskName, results.size());
    }

    private <T> List<T> execute(final Supplier<T> warmUpTask) {
        final List<T> results = new ArrayList<>();
        IntStream.range(START, END).forEach(api -> {
            try {
                T result = warmUpTask.get();
                if (result != null) {
                    results.add(result);
                }
            } catch (Exception ex) {
                log.error("Failed to warmup: {}", ex.getMessage());
            }
        });
        return results;
    }

    private Object warmUpRdb() {
        return entityManager.createNativeQuery("SELECT 1").getSingleResult();
    }

    private String warmUpRedis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.ping();
        }
    }

    private String warmUpExternalApiCaller() {
        return restTemplate.getForObject("https://www.google.com", String.class);
    }

    private Supplier<?> warmUpCouponReadApi() {
        for (int index = 1; index <= 1_000; index++) {
            couponReadApi.findCouponParticipationStatus(new DailygeUser(1L, NORMAL));
        }
        return () -> "";
    }

    private Supplier<?> warmUpEventParticipantApi() {
        for (int index = 1; index <= 1_000; index++) {
            eventCreateApi.createCouponEvent(new DailygeUser(1L, NORMAL), 1L);
        }
        return () -> "";
    }
}
