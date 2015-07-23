package dlord03.cache;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;

public class QueryServiceImpl implements QueryService {

  private final static Logger LOG = LoggerFactory.getLogger(QueryServiceImpl.class);
  private CacheManager cacheManager;
  private Properties properties;
  private Map<String, Plugin<? extends SecurityData>> plugins;

  public QueryServiceImpl() {
    super();
    plugins = new ConcurrentHashMap<>();
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /* (non-Javadoc)
   * @see dlord03.cache.Service#getCacheManager()
   */
  @Override
  public CacheManager getCacheManager() {
    return this.cacheManager;
  }

  /* (non-Javadoc)
   * @see dlord03.cache.Service#getProperties()
   */
  @Override
  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  /* (non-Javadoc)
   * @see dlord03.cache.Service#start()
   */
  @Override
  public void start() {

    // Validate provided context.
    if (cacheManager == null) throw new IllegalStateException("cacheManager can not be null");
    if (properties == null) throw new IllegalStateException("properties can not be null");

    loadPlugins();
    if (plugins == null || plugins.size() == 0)
      throw new IllegalStateException("no plugins loaded");

    // configure the cache
    MutableConfiguration<String, Integer> config =
        new MutableConfiguration<String, Integer>().setTypes(String.class, Integer.class)
            .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
            .setStatisticsEnabled(true);

    // create the cache
    Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);

    // cache operations
    String key = "key";
    Integer value1 = 1;
    cache.put("key", value1);
    Integer value2 = cache.get(key);
    cache.remove(key);


  }

  /* (non-Javadoc)
   * @see dlord03.cache.Service#stop()
   */
  @Override
  public void stop() {
    this.cacheManager.close();
  }

  private void loadPlugins() {

    plugins.clear();

    // Attempt to load each plug-in if available.
    for (CacheType cacheType : CacheType.values()) {
      String pluginType = cacheType.getName();
      String propertyName = String.format("%s.plugin.classname", pluginType);
      String className = properties.getProperty(propertyName);
      LOG.debug("Checking for '{}' plugin provider property {}={}", pluginType, propertyName, className);
      Plugin<? extends SecurityData> plugin = loadPlugin(className, cacheType);
      if (plugin != null) {
        plugins.put(pluginType, plugin);
        LOG.debug("Plugin provider loaded {}={}", pluginType, className);
      } else {
        LOG.debug("No plugin provider loaded for '{}'", pluginType);
      }
    }

  }

  @SuppressWarnings("unchecked")
  private <T extends SecurityData> Plugin<T> loadPlugin(String className, CacheType cacheType) {
    if (className == null) return null;
    Class<T> pluginClass = null;
    Plugin<T> plugin = null;
    try {
      pluginClass =  (Class<T>) Class.forName(className);
      plugin = (Plugin<T>) pluginClass.newInstance();
      plugin.registerInvalidationHandler(new InvalidationReportHandlerImpl(this, cacheType));
   } catch (ReflectiveOperationException e) {
      LOG.warn("Can not create plugin : {}", className, e);
    }
    return plugin;
  }

  @Override
  public SecurityData getLatestValue(CacheType type, SecurityIdentifier security) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SecurityData getLatestValue(CacheType type, SecurityIdentifier security, Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SecurityData getEndOfDayValue(CacheType type, SecurityIdentifier security,
      LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void handleInvalidationReport(CacheType type, InvalidationReport report) {
    // TODO Auto-generated method stub
    
  }

}
