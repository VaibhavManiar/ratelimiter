package io.vm.ratelimit.conf;

import io.vm.ratelimit.RateLimiterAlgorithm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

public class RateLimiterDefaultConfigurationLoader implements RateLimiterConfigurationLoader {

    private Map<String, RateLimiterConfiguration> configurationMap;

    public RateLimiterDefaultConfigurationLoader() {
        this.configurationMap = new HashMap<>();
    }

    @Override
    public List<RateLimiterConfiguration> load(Path configurationFilePath) {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configurationFilePath.toFile())) {
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
        String algoStr = properties.getProperty("rateLimiter.algorithm", RateLimiterAlgorithm.FIXED_SIZE.name());
        RateLimiterAlgorithm algorithm = RateLimiterAlgorithm.valueOf(algoStr);

        Set<String> rateLimiterNames = getRateLimiterNames(properties);
        rateLimiterNames.forEach(rateLimiterName -> {
            RateLimiterConfiguration configuration = loadFromProperties(rateLimiterName, algorithm, properties);
            configurationMap.put(rateLimiterName, configuration);
        });
        return new ArrayList<>(configurationMap.values());
    }

    @Override
    public List<RateLimiterConfiguration> load() {
        return load(Path.of("ratelimiter.properties"));
    }

    @Override
    public RateLimiterConfiguration getConfiguration(String rateLimiterName) {
        return configurationMap.get(rateLimiterName);
    }

    @Override
    public List<RateLimiterConfiguration> getAllConfigurations() {
        return new ArrayList<>(configurationMap.values());
    }

    private RateLimiterConfiguration loadFromProperties(String rateLimiterName, RateLimiterAlgorithm algorithm, Properties properties) {
        return switch (algorithm) {
            case SLIDING_WINDOW -> new SlidingWindowRateLimiterConfiguration(rateLimiterName, Integer.parseInt(properties.getProperty("rateLimiter."+rateLimiterName+".fillRateInSec", "1000")), Integer.parseInt(properties.getProperty("rateLimiter."+rateLimiterName+".capacity", "1000")));
            default -> new FixedSizeRateLimiterConfiguration(rateLimiterName, Integer.parseInt(properties.getProperty("rateLimiter."+rateLimiterName+".capacity", "1000")));
        };
    }

    private Set<String> getRateLimiterNames(Properties properties) {
        Set<String> names = new HashSet<>();
        properties.keySet().forEach(key -> {
            if(key.toString().startsWith("rateLimiter")) {
                String[] keySplit = String.valueOf(key).split(".");
                if(keySplit.length > 1) {
                    names.add(keySplit[1]);
                }
            }
        });
        return names;
    }


}
