import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    void allowRequest_rejectsMoreRequestsThanConfigured() {
        LocalDateTime timeNow = LocalDateTime.now();
        RateLimiter limiter = new RateLimiter(3, Duration.ofSeconds(10));
        assertTrue(limiter.allowRequest("user1", timeNow));
        assertTrue(limiter.allowRequest("user1", timeNow.plusSeconds(1)));
        assertTrue(limiter.allowRequest("user1", timeNow.plusSeconds(2)));
        assertFalse(limiter.allowRequest("user1", timeNow.plusSeconds(3)));
        assertFalse(limiter.allowRequest("user1", timeNow.plusSeconds(5)));
    }

    @Test
    void allowRequest_acceptsRequests_afterTimeExpires() {
        LocalDateTime timeNow = LocalDateTime.now();
        RateLimiter limiter = new RateLimiter(1, Duration.ofSeconds(10));
        assertTrue(limiter.allowRequest("user1", timeNow));
        assertTrue(limiter.allowRequest("user1", timeNow.plusSeconds(11)));
    }

}
