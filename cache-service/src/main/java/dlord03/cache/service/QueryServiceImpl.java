package dlord03.cache.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;

import dlord03.cache.CacheController;
import dlord03.cache.PluginController;
import dlord03.cache.QueryService;
import dlord03.cache.data.DataType;
import dlord03.cache.data.SimpleKey;
import dlord03.cache.data.SimpleKeyGenerator;
import dlord03.cache.data.TemporalKey;
import dlord03.cache.data.TemporalKeyGenerator;
import dlord03.cache.index.Index;
import dlord03.cache.index.IndexImpl;
import dlord03.cache.index.IndexKey;
import dlord03.cache.index.IndexKeyGenerator;
import dlord03.cache.index.IndexType;
import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;

public class QueryServiceImpl
  implements QueryService, PluginInvalidationReportHandler {

  private CacheManager cacheManager;
  private Properties properties;
  private PluginController pluginController;
  private CacheController cacheController;

  public QueryServiceImpl() {
    super();
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  public CacheManager getCacheManager() {
    return this.cacheManager;
  }

  public PluginController getPluginController() {
    return this.pluginController;
  }

  public CacheController getCacheController() {
    return this.cacheController;
  }

  public void setProperties(Properties properties) {
    this.properties = new Properties(properties);
  }

  public Properties getProperties() {
    return properties;
  }

  @Override
  public void start() {

    // Validate provided context.
    if (cacheManager == null) {
      throw new IllegalStateException("cacheManager can not be null");
    }
    if (properties == null) {
      throw new IllegalStateException("properties can not be null");
    }

    loadPlugins();
    if (pluginController.getPlugins().size() == 0) {
      throw new IllegalStateException("no plugins loaded");
    }

    createCache();

  }

  @Override
  public void stop() {
    if (cacheController != null) {
      cacheController.close();
    }
    if (pluginController != null) {
      pluginController.close();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends SecurityData> T getLatestValue(DataType type,
    SecurityIdentifier security) {

    // Generate the key for the requested data.
    final SimpleKey key = SimpleKeyGenerator.generate(type, security);

    // Get the latest cache.
    final Cache<SimpleKey, T> cache = cacheController.getLatestCache();

    // Is the record in the latest cache?
    T result = cache.get(key);

    // If it wasn't then ask the relevant plug-in for it.
    if (result == null) {
      result = (T) getPlugin(type).getLatestValue(security);
      // If the plug-in returned it then add it to the cache.
      if (result != null) {
        cache.put(key, result);
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends SecurityData> T getLatestValue(DataType type,
    SecurityIdentifier security, Instant before) {

    T result = null;

    // Get the intra-day cache.
    final Cache<TemporalKey, SecurityData> cache =
      cacheController.getTimestampedCache();

    // Generate the index key for this request
    final IndexKey indexKey =
      IndexKeyGenerator.generate(IndexType.INTRADAY, type, security);

    // Get the index for this request.
    final Index index = getOrCreateIndex(indexKey);

    // Do we have a key in the index?
    TemporalKey foundKey = index.getLatestKey(before);

    // If we don't then look up the data from the plug-in;
    if (foundKey == null) {

      result = (T) getPlugin(type).getLatestValue(security, before);
      if (result != null) {

        // If the plug-in returned data then add its key to the index
        foundKey = TemporalKeyGenerator.generate(type, result);
        addIndexLatestKey(indexKey, foundKey, before);

        // And add the data to the cache.
        cache.put(foundKey, result);
      }

    } else {

      result = (T) cache.get(foundKey);

    }

    return result;

  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends SecurityData> T getEndOfDayValue(DataType type,
    SecurityIdentifier security, LocalDate date) {

    T result = null;

    // Get the intra-day cache.
    final Cache<TemporalKey, SecurityData> cache =
      cacheController.getDatedCache();

    // Generate the index key for this request
    final IndexKey indexKey =
      IndexKeyGenerator.generate(IndexType.ENDOFDAY, type, security);

    // Get the index for this request.
    final Index index = getOrCreateIndex(indexKey);

    // Do we have a key in the index?
    TemporalKey foundKey = index.getEndOfDayKey(date);

    // If we don't then look up the data from the plug-in;
    if (foundKey == null) {

      result = (T) getPlugin(type).getEndOfDayValue(security, date);
      if (result != null) {

        // If the plug-in returned data then add its key to the index
        foundKey = TemporalKeyGenerator.generate(type, result);
        addIndexEndOfDayKey(indexKey, foundKey, date);

        // And add the data to the cache.
        cache.put(foundKey, result);
      }

    } else {

      result = (T) cache.get(foundKey);

    }

    return result;

  }

  @Override
  public <T extends SecurityData> void handleInvalidationReport(Class<T> type,
    InvalidationReport report) {
    // TODO Auto-generated method stub
  }

  private Plugin<? extends SecurityData> getPlugin(DataType type) {
    return pluginController.getPlugin(type.getValueClass());
  }

  private Index getOrCreateIndex(IndexKey key) {

    final Cache<IndexKey, Index> indexCache = cacheController.getIndexCache();
    Index index = indexCache.get(key);
    if (index == null) {
      index = new IndexImpl(key.getDataType(), key.getSecurityIdentifier());
      if (!indexCache.putIfAbsent(key, index)) {
        index = indexCache.get(key);
      };
    }
    return index;

  }

  private boolean addIndexLatestKey(IndexKey key, TemporalKey dataKey,
    Instant before) {

    final int maximumAttempts = 10;
    boolean success = false;

    final Cache<IndexKey, Index> indexCache = cacheController.getIndexCache();
    Index originalIndex;
    Index updatedIndex;

    for (int i = 0; !success && i < maximumAttempts; i++) {
      originalIndex = getOrCreateIndex(key);
      updatedIndex = getOrCreateIndex(key);
      updatedIndex.addLatestKey(dataKey, before);
      success = indexCache.replace(key, originalIndex, updatedIndex);

    }

    return success;

  }

  private boolean addIndexEndOfDayKey(IndexKey key, TemporalKey dataKey,
    LocalDate date) {

    final int maximumAttempts = 10;
    boolean success = false;

    final Cache<IndexKey, Index> indexCache = cacheController.getIndexCache();
    Index originalIndex;
    Index updatedIndex;

    for (int i = 0; !success && i < maximumAttempts; i++) {
      originalIndex = getOrCreateIndex(key);
      updatedIndex = getOrCreateIndex(key);
      updatedIndex.addEndOfDayKey(dataKey, date);
      success = indexCache.replace(key, originalIndex, updatedIndex);

    }

    return success;

  }

  private void loadPlugins() {

    properties.put("invalidationReportHandler", this);
    pluginController = new PluginControllerImpl(properties);
    pluginController.open();

  }

  private void createCache() {

    cacheController = new CacheControllerImp(cacheManager);
    cacheController.open();

  }

}
