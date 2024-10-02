package io.vm.ratelimit.bucket;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TimeBoundBucketImpl implements TimeBoundBucket {
    private final ReentrantLock lock;
    private final ReentrantLock refresherLock;
    private long lastUpdatedAt;
    private final AtomicInteger tokens;
    private final int capacity;
    private final long timeWindow;

    public TimeBoundBucketImpl(int capacity, long timeWindow) {
        this.capacity = capacity;
        this.timeWindow = timeWindow;
        this.tokens = new AtomicInteger(this.capacity);
        this.lastUpdatedAt = System.currentTimeMillis();
        this.lock = new ReentrantLock(true);
        this.refresherLock = new ReentrantLock(true);
    }

    public boolean isTokenAvailable() {
        boolean isAvailable = false;
        if (tokens.get() > 0) {
            lock.lock();
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                isAvailable = true;
            }
            lock.unlock();
        }
        return isAvailable;
    }

    public final void refresh() {
        long now = System.currentTimeMillis();
        if ((lastUpdatedAt + timeWindow) < now && refresherLock.tryLock()) {
            tokens.set(capacity);
            lastUpdatedAt = now;
            refresherLock.unlock();
        }
    }
}
