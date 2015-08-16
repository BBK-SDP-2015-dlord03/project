package dlord03.cache;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;

public class QueryServiceImpl implements QueryService {

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

  @Override
  public CacheManager getCacheManager() {
    return this.cacheManager;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public void start() {

    // Validate provided context.
    if (cacheManager == null)
      throw new IllegalStateException("cacheManager can not be null");
    if (properties == null) throw new IllegalStateException("properties can not be null");

    loadPlugins();
    if (pluginController.getPlugins().size() == 0)
      throw new IllegalStateException("no plugins loaded");

    createCache();

  }

  @Override
  public void stop() {
    this.cacheController.close();
    this.pluginController.close();
  }

  @Override
  public SecurityData getLatestValue(DataType type, SecurityIdentifier security) {
    final Plugin<? extends SecurityData> plugin = pluginController.getPlugin(type);
    return plugin.getLatestValue(security);
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

    pluginController = new PluginController(properties, this);
    pluginController.open();

  }

  private void createCache() {
    
    cacheController = new CacheController(cacheManager);
    cacheController.open();
    
  }


}
