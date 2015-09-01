package uk.ac.bbk.dlord03.cache.service;

import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.cache.CacheController;
import uk.ac.bbk.dlord03.cache.data.SimpleKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.index.Index;
import uk.ac.bbk.dlord03.cache.index.IndexKey;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

/**
 * Object for creating and managing access to the underlying cache
 * implementations.
 * 
 * @author David Lord
 *
 */
@SuppressWarnings("unchecked")
public class CacheControllerImp implements CacheController {

  private final CacheManager cacheManager;
  private Cache<IndexKey, Index> indexCache;
  private Cache<SimpleKey, SecurityData> latestCache;
  private Cache<TemporalKey, SecurityData> timestampedCache;
  private Cache<TemporalKey, SecurityData> datedCache;

  public CacheControllerImp(CacheManager cacheManager) {
    super();
    this.cacheManager = cacheManager;
  }

  @Override
  public void open() {

    // configure the index cache
    final MutableConfiguration<IndexKey, Index> indexConfig =
          new MutableConfiguration<IndexKey, Index>()
                .setTypes(IndexKey.class, Index.class)
                .setExpiryPolicyFactory(
                      AccessedExpiryPolicy.factoryOf(Duration.ONE_DAY))
                .setStatisticsEnabled(true);

    // create the index cache
    indexCache = cacheManager.createCache("indexCache", indexConfig);

    // configure the latest cache
    final MutableConfiguration<SimpleKey, SecurityData> latestConfig =
          new MutableConfiguration<SimpleKey, SecurityData>()
                .setTypes(SimpleKey.class, SecurityData.class)
                .setExpiryPolicyFactory(
                      AccessedExpiryPolicy.factoryOf(Duration.ONE_DAY))
                .setStatisticsEnabled(true);

    // create the latest cache
    latestCache = cacheManager.createCache("latestCache", latestConfig);

    // configure the time stamped cache
    final MutableConfiguration<TemporalKey, SecurityData> timestampedConfig =
          new MutableConfiguration<TemporalKey, SecurityData>()
                .setTypes(TemporalKey.class, SecurityData.class)
                .setExpiryPolicyFactory(
                      AccessedExpiryPolicy.factoryOf(Duration.ONE_DAY))
                .setStatisticsEnabled(true);

    // create the time stamped cache
    timestampedCache =
          cacheManager.createCache("timestampedCache", timestampedConfig);

    // configure the dated cache
    final MutableConfiguration<TemporalKey, SecurityData> datedConfig =
          new MutableConfiguration<TemporalKey, SecurityData>()
                .setTypes(TemporalKey.class, SecurityData.class)
                .setExpiryPolicyFactory(
                      AccessedExpiryPolicy.factoryOf(Duration.ONE_DAY))
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
  public Cache<SimpleKey, SecurityData> getLatestCache() {
    return latestCache;
  }

  @Override
  public Cache<TemporalKey, SecurityData> getTimestampedCache() {
    return timestampedCache;
  }

  @Override
  public Cache<TemporalKey, SecurityData> getDatedCache() {
    return datedCache;
  }

}
