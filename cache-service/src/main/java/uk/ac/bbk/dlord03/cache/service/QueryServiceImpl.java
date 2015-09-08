package uk.ac.bbk.dlord03.cache.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.CacheController;
import uk.ac.bbk.dlord03.cache.PluginController;
import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.SimpleKey;
import uk.ac.bbk.dlord03.cache.data.SimpleKeyGenerator;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyGenerator;
import uk.ac.bbk.dlord03.cache.index.Index;
import uk.ac.bbk.dlord03.cache.index.IndexImpl;
import uk.ac.bbk.dlord03.cache.index.IndexKey;
import uk.ac.bbk.dlord03.cache.index.IndexKeyGenerator;
import uk.ac.bbk.dlord03.cache.index.IndexType;
import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReport;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;

public class QueryServiceImpl
      implements QueryService, PluginInvalidationReportHandler {

  private static final Logger LOG =
        LoggerFactory.getLogger(QueryServiceImpl.class);

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

  public PluginController getPluginController() {
    return this.pluginController;
  }

  public void setProperties(final Properties properties) {
    this.properties = new Properties();
    if (properties != null && properties.size() > 0) {
      this.properties.putAll(properties);
    }
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
      LOG.info("Destroying caches...");
      cacheController.close();
      LOG.info("Caches destroyed.");
    }
    if (pluginController != null) {
      LOG.info("Unloading plugins...");
      pluginController.close();
      LOG.info("Plugins unloaded.");
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
    String typeName = type.toString().toLowerCase();
    if (result == null) {
      LOG.info("Querying {} plugin for latest value of {}.", typeName,
            security);
      result = (T) getPlugin(type).getLatestValue(security);

      // If the plug-in returned it then add it to the cache.
      if (result != null) {
        LOG.info("Retrieved {} record from plugin.", typeName);
        cache.put(key, result);
      }
    } else {
      LOG.info("Found result in cache.");
    }

    if (result == null) {
      LOG.info("Record not found.");
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
    String typeName = type.toString().toLowerCase();
    if (foundKey == null) {

      LOG.info("Querying {} plugin for latest value of {} before {}.", typeName,
            security, before);

      result = (T) getPlugin(type).getLatestValue(security, before);
      if (result != null) {

        LOG.info("Retrieved {} record from plugin.", typeName);

        // If the plug-in returned data then add its key to the index
        foundKey = TemporalKeyGenerator.generate(type, result);
        addIndexLatestKey(indexKey, foundKey, before);

        // And add the data to the cache.
        cache.put(foundKey, result);
      }

    } else {

      LOG.info("Found qualifying previous {} result in index.", typeName);
      result = (T) cache.get(foundKey);

    }

    if (result == null) {
      LOG.info("Record not found.");
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
    String typeName = type.toString().toLowerCase();
    if (foundKey == null) {

      LOG.info("Querying {} plugin for latest value of {} before {}.", typeName,
            security, date);

      result = (T) getPlugin(type).getEndOfDayValue(security, date);
      if (result != null) {

        LOG.info("Retrieved {} record from plugin.", typeName);

        // If the plug-in returned data then add its key to the index
        foundKey = TemporalKeyGenerator.generate(type, result);
        addIndexEndOfDayKey(indexKey, foundKey, date);

        // And add the data to the cache.
        cache.put(foundKey, result);
      }

    } else {

      LOG.info("Found qualifying previous {} result in index.", typeName);
      result = (T) cache.get(foundKey);

    }

    if (result == null) {
      LOG.info("Record not found.");
    }
    return result;

  }

  @Override
  public <T extends SecurityData> void handleInvalidationReport(Class<T> type,
        InvalidationReport report) {

    // Generate the simple key for the invalidated data.
    SecurityIdentifier security = report.getInvalidatedSecurity();
    SimpleKey key =
          SimpleKeyGenerator.generate(DataType.valueOf(type), security);
    DataType dataType = DataType.valueOf(type);

    // Get the latest data cache and remove this value if it is there.
    final Cache<SimpleKey, T> latestCache = cacheController.getLatestCache();
    T latestEntry = latestCache.getAndRemove(key);

    // If it was there now add it to the intra-day cache.
    if (latestEntry != null) {
      final Cache<TemporalKey, SecurityData> intraDayCache;
      intraDayCache = cacheController.getTimestampedCache();

      // If the plug-in returned data then add its key to the index
      TemporalKey newKey = TemporalKeyGenerator.generate(dataType, latestEntry);

      // Generate the index key for this request
      final IndexKey indexKey =
            IndexKeyGenerator.generate(IndexType.INTRADAY, dataType, security);

      // The predicate for the intra-day value is now as it was valid until now.
      Instant predicate = Instant.now();

      addIndexLatestKey(indexKey, newKey, predicate);

      // And add the data to the intra-day cache.
      intraDayCache.put(newKey, latestEntry);

    }

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
      }
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
      if (success)
        break;
    }

    if (!success)
      LOG.warn("Index update failure.");

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
      if (success)
        break;
    }

    if (!success)
      LOG.warn("Index update failure.");

    return success;

  }

  private void loadPlugins() {
    LOG.info("Loading plugins...");
    properties.put("invalidationReportHandler", this);
    pluginController = new PluginControllerImpl(properties);
    pluginController.open();
    LOG.info("Plugins loaded.");
  }

  private void createCache() {
    LOG.info("Creating caches...");
    cacheController = new CacheControllerImp(cacheManager);
    cacheController.open();
    LOG.info("Caches created.");
  }

}
