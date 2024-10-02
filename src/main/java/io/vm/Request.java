package io.vm;

import io.vm.ratelimit.RateLimiterAlgorithm;

public class Request<T> {
    private final String id;
    private final T payload;
    private final long timestamp;
    private final RateLimiterAlgorithm ratelimitBy;

    public Request(String id, T payload, RateLimiterAlgorithm ratelimitBy) {
        this.id = id;
        this.payload = payload;
        this.ratelimitBy = ratelimitBy;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public T getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public RateLimiterAlgorithm getRatelimitBy() {
        return this.ratelimitBy;
    }
}
