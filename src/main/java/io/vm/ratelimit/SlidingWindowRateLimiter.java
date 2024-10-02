package io.vm.ratelimit;

import io.vm.Request;
import io.vm.ratelimit.bucket.TimeBoundBucket;
import io.vm.ratelimit.bucket.TimeBoundBucketImpl;
import io.vm.ratelimit.bucket.TokenNotFoundException;
import io.vm.ratelimit.conf.RateLimiterConfiguration;
import io.vm.ratelimit.conf.SlidingWindowRateLimiterConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SlidingWindowRateLimiter<Req extends Request<?>, Resp> implements RateLimiter<Req, Resp> {
    private final Map<String, TimeBoundBucket> bucketStore = new ConcurrentHashMap<>();
    private final long timeWindow;
    private final int capacity;

    public SlidingWindowRateLimiter(RateLimiterConfiguration configuration) {
        SlidingWindowRateLimiterConfiguration conf = (SlidingWindowRateLimiterConfiguration) configuration;
        this.capacity = conf.getCapacity();
        this.timeWindow = conf.getWindowSize();
    }


    public Resp allow(String customerId, Req request, Function<Req, Resp> func) {
        if (getBucket(customerId).isTokenAvailable()) {
            return func.apply(request);
        }
        throw new TokenNotFoundException(request.getId());
    }

    public RateLimiterAlgorithm getType() {
        return RateLimiterAlgorithm.SLIDING_WINDOW;
    }

    private TimeBoundBucket getBucket(String customerId) {
        if (bucketStore.containsKey(customerId)) {
            TimeBoundBucket bucket = bucketStore.get(customerId);
            bucket.refresh();
            return bucket;
        } else {
            TimeBoundBucket bucket = new TimeBoundBucketImpl(capacity, timeWindow);
            bucketStore.put(customerId, bucket);
            return bucket;
        }
    }
}
