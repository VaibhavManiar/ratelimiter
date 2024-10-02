package io.vm.ratelimit;/* API ratelimiter.RateLimiter */

import java.util.function.Function;

public interface RateLimiter<Req, Resp> {
    Resp allow(String customerId, Req req, Function<Req, Resp> func);
    RateLimiterAlgorithm getType();
}



