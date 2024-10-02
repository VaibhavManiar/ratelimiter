package io.vm.ratelimit;

import io.vm.Request;
import io.vm.ratelimit.bucket.SemaphoreBucket;
import io.vm.ratelimit.bucket.SemaphoreBucketImpl;
import io.vm.ratelimit.bucket.TokenNotFoundException;
import io.vm.ratelimit.conf.RateLimiterConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FixedSizeRateLimiterTest {

    private FixedSizeRateLimiter<Request<String>, String> rateLimiter;
    private RateLimiterConfiguration configuration;
    private SemaphoreBucket bucket;
    private Function<Request<String>, String> function;

    @BeforeEach
    void setUp() {
        configuration = mock(RateLimiterConfiguration.class);
        when(configuration.getCapacity()).thenReturn(1);
        when(configuration.getName()).thenReturn("rl1");
        rateLimiter = new FixedSizeRateLimiter<>(configuration);
        bucket = mock(SemaphoreBucketImpl.class);
        function = mock(Function.class);
    }

    @Test
    void testAllow_Positive() {
        when(bucket.isTokenAvailable()).thenReturn(true);
        when(function.apply(any())).thenReturn("Success");

        //rateLimiter.tokenStore.put("customer1", bucket);
        Request<String> request = new RateLimitedRequest<>("req1", "payload", "rl1", RateLimiterAlgorithm.FIXED_SIZE);
        rateLimiter.allow("customer1", request, function);
        rateLimiter.allow("customer1", request, function);
        String response = rateLimiter.allow("customer1", request, function);

        assertEquals("Success", response);
    }

    @Test
    void testAllow_Negative() {
        when(bucket.getAvailableTokenCount()).thenReturn(0);
        when(bucket.isTokenAvailable()).thenReturn(false);
        when(configuration.getCapacity()).thenReturn(0);
        rateLimiter = new FixedSizeRateLimiter<>(configuration);

        //rateLimiter.tokenStore.put("customer1", bucket);
        Request<String> request = new RateLimitedRequest<>("req1", "payload", "rl2", RateLimiterAlgorithm.FIXED_SIZE);

        assertThrows(TokenNotFoundException.class, () -> rateLimiter.allow("customer1", request, function));
    }

    @Test
    void testAllow_NewCustomer() {
        Request<String> request = new RateLimitedRequest<>("req1", "payload", "rl1", RateLimiterAlgorithm.FIXED_SIZE);
        when(function.apply(any())).thenReturn("Success");

        String response = rateLimiter.allow("customer2", request, function);

        assertEquals("Success", response);
        //assertTrue(rateLimiter.tokenStore.containsKey("customer2"));
    }

    @Test
    void testGetType() {
        assertEquals(RateLimiterAlgorithm.FIXED_SIZE, rateLimiter.getType());
    }
}