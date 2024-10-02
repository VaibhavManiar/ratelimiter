package io.vm.ratelimit.bucket;

import java.util.concurrent.Semaphore;

public class SemaphoreBucketImpl implements SemaphoreBucket {
    private final Semaphore semaphore;

    public SemaphoreBucketImpl(int capacity) {
        this.semaphore = new Semaphore(capacity);
    }

    public boolean isTokenAvailable() {
        return this.semaphore.tryAcquire();
    }

    public void release() {
        this.semaphore.release();
    }

    public int getAvailableTokenCount() {
        return this.semaphore.availablePermits();
    }
}
