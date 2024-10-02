package io.vm.ratelimit.conf;

import io.vm.ratelimit.RateLimiterAlgorithm;

public interface RateLimiterConfiguration {
    String getName();
    RateLimiterAlgorithm getAlgorithm();
    int getCapacity();
}
