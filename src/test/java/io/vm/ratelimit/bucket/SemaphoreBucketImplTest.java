package io.vm.ratelimit.bucket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemaphoreBucketImplTest {

    private SemaphoreBucketImpl semaphoreBucket;

    @BeforeEach
    void setUp() {
        semaphoreBucket = new SemaphoreBucketImpl(3); // Initialize with a capacity of 3
    }

    @Test
    void testIsTokenAvailable_Positive() {
        assertTrue(semaphoreBucket.isTokenAvailable());
        assertTrue(semaphoreBucket.isTokenAvailable());
        assertTrue(semaphoreBucket.isTokenAvailable());
    }

    @Test
    void testIsTokenAvailable_Negative() {
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.isTokenAvailable();
        assertFalse(semaphoreBucket.isTokenAvailable()); // No more tokens available
    }

    @Test
    void testRelease_Positive() {
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.release();
        assertTrue(semaphoreBucket.isTokenAvailable()); // Token should be available after release
    }

    @Test
    void testRelease_Negative() {
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.isTokenAvailable();
        semaphoreBucket.isTokenAvailable();
        assertFalse(semaphoreBucket.isTokenAvailable());
        semaphoreBucket.release();
        semaphoreBucket.release();
        semaphoreBucket.release();
        assertTrue(semaphoreBucket.isTokenAvailable());
        assertTrue(semaphoreBucket.isTokenAvailable());
        assertTrue(semaphoreBucket.isTokenAvailable());
        assertFalse(semaphoreBucket.isTokenAvailable());
    }

    @Test
    void testGetAvailableTokenCount() {
        assertEquals(3, semaphoreBucket.getAvailableTokenCount());
        semaphoreBucket.isTokenAvailable();
        assertEquals(2, semaphoreBucket.getAvailableTokenCount());
        semaphoreBucket.isTokenAvailable();
        assertEquals(1, semaphoreBucket.getAvailableTokenCount());
        semaphoreBucket.release();
        assertEquals(2, semaphoreBucket.getAvailableTokenCount());
    }
}