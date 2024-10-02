package io.vm.ratelimit.conf;

import java.nio.file.Path;
import java.util.List;

public interface RateLimiterConfigurationLoader {
    List<RateLimiterConfiguration> load(Path configurationFilePath);
    List<RateLimiterConfiguration> load();
    RateLimiterConfiguration getConfiguration(String rateLimiterName);
    List<RateLimiterConfiguration> getAllConfigurations();
}