package dlord03.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

public class Service {

  private final CachingProvider cachingProvider;
  private final CacheManager cacheManager;

  public Service() {

    super();

    // resolve a cache manager
    cachingProvider = Caching.getCachingProvider();
    cacheManager = cachingProvider.getCacheManager();

    // configure the cache
    MutableConfiguration<String, Integer> config =
      new MutableConfiguration<String, Integer>()
        .setTypes(String.class, Integer.class)
        .setExpiryPolicyFactory(
          AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
        .setStatisticsEnabled(true);

    // create the cache
    Cache<String, Integer> cache =
      cacheManager.createCache("simpleCache", config);

    // cache operations
    String key = "key";
    Integer value1 = 1;
    cache.put("key", value1);
    Integer value2 = cache.get(key);
    cache.remove(key);

  }

}
