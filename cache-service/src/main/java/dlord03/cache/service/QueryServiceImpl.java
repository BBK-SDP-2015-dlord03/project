package dlord03.cache.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.cache.CacheController;
import dlord03.cache.PluginController;
import dlord03.cache.QueryService;
import dlord03.cache.data.DataType;
import dlord03.cache.data.SimpleKey;
import dlord03.cache.data.SimpleKeyGenerator;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;

public class QueryServiceImpl implements QueryService, PluginInvalidationReportHandler {

  private final static Logger LOG = LoggerFactory.getLogger(QueryServiceImpl.class);
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
    if (cacheManager == null)
      throw new IllegalStateException("cacheManager can not be null");
    if (properties == null)
      throw new IllegalStateException("properties can not be null");

    loadPlugins();
    if (pluginController.getPlugins().size() == 0)
      throw new IllegalStateException("no plugins loaded");

    createCache();

  }

  @Override
  public void stop() {
    if (cacheController != null)
      cacheController.close();
    if (pluginController != null)
      pluginController.close();
  }

  @Override
  public SecurityData getLatestValue(DataType type, SecurityIdentifier security) {

    // Get the key for the requested data.
    SimpleKey key = SimpleKeyGenerator.generate(type, security);

    // Get the latest cache.
    Cache<SimpleKey, SecurityData> cache = cacheController.getLatestCache();

    // Is the record in the latest cache?
    SecurityData result = cache.get(key);

    // If it wasn't then ask the relevant plug-in for it.
    if (result == null) {
      result = pluginController.getPlugin(type).getLatestValue(security);
      // If the plug-in returned it then add it to the cache.
      if (result != null) {
        cache.put(key, result);
      }
    }

    return result;
  }

  @Override
  public SecurityData getLatestValue(DataType type, SecurityIdentifier security,
    Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SecurityData getEndOfDayValue(DataType type, SecurityIdentifier security,
    LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void handleInvalidationReport(DataType type, InvalidationReport report) {
    // TODO Auto-generated method stub
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
