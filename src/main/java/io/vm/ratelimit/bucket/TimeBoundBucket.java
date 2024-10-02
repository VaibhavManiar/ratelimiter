package io.vm.ratelimit.bucket;

public interface TimeBoundBucket {
    boolean isTokenAvailable();

    void refresh();
}
