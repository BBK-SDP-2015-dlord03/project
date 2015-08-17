package dlord03.cache.service;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.cache.PluginController;
import dlord03.cache.data.DataType;
import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;

public class PluginControllerImpl implements PluginController {

  private final static Logger LOG = LoggerFactory.getLogger(PluginControllerImpl.class);

  private final Properties properties;
  private final Map<String, Plugin<? extends SecurityData>> plugins;
  private PluginInvalidationReportHandler invalidationReportHandler;

  public PluginControllerImpl(Properties properties) {
    super();
    this.properties = new Properties(properties);
    this.plugins = new ConcurrentHashMap<>();
    Object reportHandler = this.properties.get("invalidationReportHandler");
    if (reportHandler != null
      && reportHandler instanceof PluginInvalidationReportHandler) {
      this.invalidationReportHandler = (PluginInvalidationReportHandler) reportHandler;
    }
  }

  @Override
  public void open() {

    plugins.clear();

    // Attempt to load each plug-in if available.
    for (final DataType dataType : DataType.values()) {
      final String pluginType = dataType.getName();
      final String propertyName = String.format("%s.plugin.classname", pluginType);
      final String className = properties.getProperty(propertyName);
      LOG.debug("Checking for '{}' plugin provider property {}={}", pluginType,
        propertyName, className);
      final Plugin<? extends SecurityData> plugin = loadPlugin(className, dataType);
      if (plugin != null) {
        plugins.put(pluginType, plugin);
        LOG.debug("Plugin provider loaded {}={}", pluginType, className);
      } else {
        LOG.debug("No plugin provider loaded for '{}'", pluginType);
      }
    }

  }

  @Override
  public void close() {
    for (Plugin<? extends SecurityData> plugin : plugins.values()) {
      plugin.close();
    }
    plugins.clear();
  }

  @Override
  public Plugin<? extends SecurityData> getPlugin(DataType dataType) {
    return plugins.get(dataType.getName());
  }

  @Override
  public Collection<Plugin<? extends SecurityData>> getPlugins() {
    return plugins.values();
  }

  @SuppressWarnings("unchecked")
  private <T extends SecurityData> Plugin<T> loadPlugin(String className,
    DataType dataType) {

    if (className == null)
      return null;
    Class<T> pluginClass = null;
    Plugin<T> plugin = null;
    try {
      pluginClass = (Class<T>) Class.forName(className);
      plugin = (Plugin<T>) pluginClass.newInstance();
      plugin.registerInvalidationHandler(
        new InvalidationReportHandlerImpl(invalidationReportHandler, dataType));
    } catch (final ReflectiveOperationException e) {
      LOG.warn("Can not create plugin : {}", className, e);
    }
    return plugin;
  }

}
