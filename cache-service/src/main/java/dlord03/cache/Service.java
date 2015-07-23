package dlord03.cache;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;

public class Service {

  private final static Logger LOG = LoggerFactory.getLogger(Service.class);
  private CacheManager cacheManager;
  private Properties properties;
  private Map<String, Plugin<? extends SecurityData>> plugins;

  public Service() {
    super();
    plugins = new ConcurrentHashMap<>();
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  public CacheManager getCacheManager() {
    return this.cacheManager;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public void start() {
    
    // Validate provided context.
    if (cacheManager == null) throw new IllegalStateException("cacheManager can not be null");
    if (properties == null) throw new IllegalStateException("properties can not be null");

    loadPlugins();
    if (plugins == null || plugins.size() == 0) throw new IllegalStateException("no plugins loaded");
    
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

  public void stop() {
    this.cacheManager.close();
  }

  private void loadPlugins() {
    
    plugins.clear();
    
    // List of possible plug-in providers.
    final String[] pluginTypes = {"volatility", "dividend", "option", "yield", "security"};
    
    // Attempt to load each plug-in if available.
    for (String pluginType : pluginTypes) {
      LOG.debug("Checking for '{}' plugin provider", pluginType);
      String className = properties.getProperty(String.format("%s.plugin.classname", pluginType));
      Plugin<? extends SecurityData> plugin = loadPlugin(className);
      if (plugin != null) {
        plugins.put(pluginType, plugin);
        LOG.debug("Plugin provider loaded {}={}", pluginType, className);
      } else {
        LOG.debug("No plugin provider loaded for '{}'", pluginType);
      }
    }
    

  }

  @SuppressWarnings("unchecked")
  private Plugin<? extends SecurityData> loadPlugin(String className) {
    if (className == null) return null;
    Class<? extends SecurityData> pluginClass = null;
    Plugin<? extends SecurityData> plugin = null;
    try {
      pluginClass = (Class<? extends SecurityData>) Class.forName(className);
      plugin = (Plugin<? extends SecurityData>) pluginClass.newInstance();   
    } catch (ReflectiveOperationException e) {
      LOG.warn("Can not create plugin : {}", className, e);
    }
    return plugin;
  }

}
