import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class RateLimiter {

    private final int maxRequests;
    private final Duration window;

    private final ConcurrentHashMap<String, Deque<LocalDateTime>> usersLimits = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, Duration window) {
        this.maxRequests = maxRequests;
        this.window = window;
    }

    public boolean allowRequest(String userId, LocalDateTime timeNow) {
        boolean[] result = {false};
        usersLimits.compute(userId, (key, queue) -> {
            if (queue == null) {
                queue = new ArrayDeque<>();
            }
            LocalDateTime windowStart = timeNow.minus(window);
            while (!queue.isEmpty() && queue.peekFirst().isBefore(windowStart)) {
                queue.pollFirst();
            }
            if (queue.size() < maxRequests) {
                queue.addLast(timeNow);
                result[0] = true;
            }
            return queue;
        });
        return result[0];
    }
}