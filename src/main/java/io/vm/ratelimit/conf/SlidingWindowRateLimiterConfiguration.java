package io.vm.ratelimit.conf;

import io.vm.ratelimit.RateLimiterAlgorithm;

public class SlidingWindowRateLimiterConfiguration implements RateLimiterConfiguration {
    private final String name;
    private final int windowSize;
    private final int capacity;

    public SlidingWindowRateLimiterConfiguration(String name, int windowSize, int limit) {
        this.name = name;
        this.windowSize = windowSize;
        this.capacity = limit;
    }

    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RateLimiterAlgorithm getAlgorithm() {
        return RateLimiterAlgorithm.SLIDING_WINDOW;
    }
}