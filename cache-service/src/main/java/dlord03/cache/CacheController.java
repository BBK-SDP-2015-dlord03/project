package dlord03.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

import dlord03.cache.data.DataKey;
import dlord03.cache.index.Index;
import dlord03.cache.index.IndexKey;
import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;

/**
 * Object for creating and managing access to the underlying cache implementation.
 * 
 * @author david
 *
 */
public class CacheController {

  private final CacheManager cacheManager;
  private Cache<IndexKey, Index> indexCache;
  private Cache<DataKey, SecurityData> latestCache;
  private Cache<DataKey, SecurityData> timestampedCache;
  private Cache<DataKey, SecurityData> datedCache;

  public CacheController(CacheManager cacheManager) {
    super();
    this.cacheManager = cacheManager;
  }

  public void open() {

    // configure the index cache
    final MutableConfiguration<IndexKey, Index> indexConfig =
        new MutableConfiguration<IndexKey, Index>().setTypes(IndexKey.class, Index.class)
            .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
            .setStatisticsEnabled(true);

    // create the index cache
    indexCache = cacheManager.createCache("indexCache", indexConfig);

    // configure the latest cache
    final MutableConfiguration<DataKey, SecurityData> latestConfig =
        new MutableConfiguration<DataKey, SecurityData>()
            .setTypes(DataKey.class, SecurityData.class)
            .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
            .setStatisticsEnabled(true);

    // create the index cache
    latestCache = cacheManager.createCache("latestCache", latestConfig);

    // configure the timestamped cache
    final MutableConfiguration<DataKey, SecurityData> timestampedConfig =
        new MutableConfiguration<DataKey, SecurityData>()
            .setTypes(DataKey.class, SecurityData.class)
            .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
            .setStatisticsEnabled(true);

    // create the index cache
    timestampedCache = cacheManager.createCache("timestampedCache", timestampedConfig);

    // configure the dated cache
    final MutableConfiguration<DataKey, SecurityData> datedConfig =
        new MutableConfiguration<DataKey, SecurityData>()
            .setTypes(DataKey.class, SecurityData.class)
            .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
            .setStatisticsEnabled(true);

    // create the index cache
    datedCache = cacheManager.createCache("datedCache", datedConfig);

  }

  public void close() {
    cacheManager.close();
  }

  public Cache<IndexKey, Index> getIndexCache() {
    return indexCache;
  }

  public Cache<DataKey, SecurityData> getLatestCache() {
    return latestCache;
  }

  public Cache<DataKey, SecurityData> getTimestampedCache() {
    return timestampedCache;
  }

  public Cache<DataKey, SecurityData> getDatedCache() {
    return datedCache;
  }


}
