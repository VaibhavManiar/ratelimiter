package io.vm.ratelimit;

import io.vm.Request;

public class RateLimitedRequest<T> extends Request<T> {
    private final String rateLimiterName;

    public RateLimitedRequest(String id, T payload, String rateLimiterName, RateLimiterAlgorithm ratelimitBy) {
        super(id, payload, ratelimitBy);
        this.rateLimiterName = rateLimiterName;
    }

    public String getRateLimiterName() {
        return rateLimiterName;
    }
}
