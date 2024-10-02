package io.vm.ratelimit.conf;

import io.vm.ratelimit.RateLimiterAlgorithm;

public class FixedSizeRateLimiterConfiguration implements RateLimiterConfiguration {
    private final int capacity;
    private final String name;

    public FixedSizeRateLimiterConfiguration(String name, int capacity) {
        this.capacity = capacity;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public RateLimiterAlgorithm getAlgorithm() {
        return RateLimiterAlgorithm.FIXED_SIZE;
    }


}
