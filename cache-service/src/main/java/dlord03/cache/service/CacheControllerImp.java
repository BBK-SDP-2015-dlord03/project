package dlord03.cache.service;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

import dlord03.cache.CacheController;
import dlord03.cache.data.TemporalDataKey;
import dlord03.cache.index.Index;
import dlord03.cache.index.IndexKey;
import dlord03.plugin.api.data.SecurityData;

/**
 * Object for creating and managing access to the underlying cache implementations.
 * 
 * @author David Lord
 *
 */
public class CacheControllerImp implements CacheController {

  private final CacheManager cacheManager;
  private Cache<IndexKey, Index> indexCache;
  private Cache<TemporalDataKey, SecurityData> latestCache;
  private Cache<TemporalDataKey, SecurityData> timestampedCache;
  private Cache<TemporalDataKey, SecurityData> datedCache;

  public CacheControllerImp(CacheManager cacheManager) {
    super();
    this.cacheManager = cacheManager;
  }

  @Override
  public void open() {

    // configure the index cache
    final MutableConfiguration<IndexKey, Index> indexConfig =
      new MutableConfiguration<IndexKey, Index>().setTypes(IndexKey.class, Index.class)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
        .setStatisticsEnabled(true);

    // create the index cache
    indexCache = cacheManager.createCache("indexCache", indexConfig);

    // configure the latest cache
    final MutableConfiguration<TemporalDataKey, SecurityData> latestConfig =
      new MutableConfiguration<TemporalDataKey, SecurityData>()
        .setTypes(TemporalDataKey.class, SecurityData.class)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
        .setStatisticsEnabled(true);

    // create the latest cache
    latestCache = cacheManager.createCache("latestCache", latestConfig);

    // configure the time stamped cache
    final MutableConfiguration<TemporalDataKey, SecurityData> timestampedConfig =
      new MutableConfiguration<TemporalDataKey, SecurityData>()
        .setTypes(TemporalDataKey.class, SecurityData.class)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
        .setStatisticsEnabled(true);

    // create the time stamped cache
    timestampedCache = cacheManager.createCache("timestampedCache", timestampedConfig);

    // configure the dated cache
    final MutableConfiguration<TemporalDataKey, SecurityData> datedConfig =
      new MutableConfiguration<TemporalDataKey, SecurityData>()
        .setTypes(TemporalDataKey.class, SecurityData.class)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
        .setStatisticsEnabled(true);

    // create the dated cache
    datedCache = cacheManager.createCache("datedCache", datedConfig);

  }

  @Override
  public void close() {
    cacheManager.close();
  }

  @Override
  public Cache<IndexKey, Index> getIndexCache() {
    return indexCache;
  }

  @Override
  public Cache<TemporalDataKey, SecurityData> getLatestCache() {
    return latestCache;
  }

  @Override
  public Cache<TemporalDataKey, SecurityData> getTimestampedCache() {
    return timestampedCache;
  }

  @Override
  public Cache<TemporalDataKey, SecurityData> getDatedCache() {
    return datedCache;
  }

}
