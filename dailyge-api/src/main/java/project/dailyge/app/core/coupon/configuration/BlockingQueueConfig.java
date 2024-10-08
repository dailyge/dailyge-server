package project.dailyge.app.core.coupon.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Configuration
public class BlockingQueueConfig {

    @Value("${queue.type:linked}")
    private String queueType;

    @Value("${queue.capacity:10000}")
    private int queueCapacity;

    private static final String LINKED_QUEUE = "linked";
    private static final String ARRAY_QUEUE = "array";
    private static final String PRIORITY_QUEUE = "priority";

    @Bean
    public BlockingQueue<CouponEventParticipant> couponEventParticipantQueue() {
        if (ARRAY_QUEUE.equals(queueType)) {
            return new ArrayBlockingQueue<>(queueCapacity);
        }
        if (LINKED_QUEUE.equals(queueType)) {
            return new LinkedBlockingQueue<>();
        }
        if (PRIORITY_QUEUE.equals(queueType)) {
            return new PriorityBlockingQueue<>();
        }
        return new LinkedBlockingQueue<>();
    }
}
