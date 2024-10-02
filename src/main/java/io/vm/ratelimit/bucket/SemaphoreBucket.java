package io.vm.ratelimit.bucket;

public interface SemaphoreBucket {
    boolean isTokenAvailable();
    void release();
    int getAvailableTokenCount();
}
