package io.vm.ratelimit;

import io.vm.ratelimit.conf.RateLimiterConfigurationLoader;
import io.vm.ratelimit.conf.RateLimiterDefaultConfigurationLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RateLimiters<Req extends RateLimitedRequest<?>, Resp> implements RateLimiter<Req, Resp> {
    private final Map<String, RateLimiter<Req, Resp>> rateLimiterMap;

    public RateLimiters() {
        this.rateLimiterMap = new ConcurrentHashMap<>();
    }

    @Override
    public Resp allow(String customerId, Req req, Function<Req, Resp> func) {
        return rateLimiterMap.get(req.getRateLimiterName()).allow(customerId, req, func);
    }

    @Override
    public RateLimiterAlgorithm getType() {
        throw new RuntimeException("Not supported");
    }

    private void init() {
        RateLimiterConfigurationLoader loader = new RateLimiterDefaultConfigurationLoader();
        loader.load().forEach(configuration -> {
            RateLimiter<Req, Resp> rateLimiter = switch (configuration.getAlgorithm()) {
                case SLIDING_WINDOW -> new SlidingWindowRateLimiter<>(configuration);
                case FIXED_SIZE -> new FixedSizeRateLimiter<>(configuration);
            };
            rateLimiterMap.put(configuration.getName(), rateLimiter);
        });
    }
}
