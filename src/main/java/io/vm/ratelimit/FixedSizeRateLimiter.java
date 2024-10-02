package io.vm.ratelimit;

import io.vm.Request;
import io.vm.ratelimit.bucket.SemaphoreBucket;
import io.vm.ratelimit.bucket.SemaphoreBucketImpl;
import io.vm.ratelimit.bucket.TokenNotFoundException;
import io.vm.ratelimit.conf.RateLimiterConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FixedSizeRateLimiter<Req extends Request<?>, Resp> implements RateLimiter<Req, Resp> {

    private final Map<String, SemaphoreBucket> tokenStore = new ConcurrentHashMap<>();
    private final int capacity;

    public FixedSizeRateLimiter(RateLimiterConfiguration configuration) {
        this.capacity = configuration.getCapacity();
    }

    public Resp allow(String customerId, Req req, Function<Req, Resp> func) {
        tokenStore.computeIfAbsent(customerId, key -> new SemaphoreBucketImpl(this.capacity));
        SemaphoreBucket bucket = tokenStore.get(customerId);
        if (bucket.isTokenAvailable()) {
            try {
                return func.apply(req);
            } finally {
                try {bucket.release();} catch (Exception ignore) {}
            }
        }
        throw new TokenNotFoundException(req.getId());
    }

    public RateLimiterAlgorithm getType() {
        return RateLimiterAlgorithm.FIXED_SIZE;
    }
}
