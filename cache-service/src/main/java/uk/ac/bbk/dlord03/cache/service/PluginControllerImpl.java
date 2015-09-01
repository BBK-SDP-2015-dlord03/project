package uk.ac.bbk.dlord03.cache.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.PluginController;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PluginControllerImpl implements PluginController {

  private static final Logger LOG =
        LoggerFactory.getLogger(PluginControllerImpl.class);

  private final Properties properties;
  private final Map<Class<? extends SecurityData>, Plugin<? extends SecurityData>> plugins;
  private PluginInvalidationReportHandler invalidationHandler;

  public PluginControllerImpl(final Properties properties) {

    super();

    this.plugins = new ConcurrentHashMap<>();
    this.properties = new Properties();
    if (properties != null && properties.size() > 0) {
      this.properties.putAll(properties);
    }

    final Object reportHandler =
          this.properties.get("invalidationReportHandler");

    if (reportHandler != null
          && reportHandler instanceof PluginInvalidationReportHandler) {
      this.invalidationHandler =
            (PluginInvalidationReportHandler) reportHandler;
    }
  }

  @Override
  public void open() {

    plugins.clear();

    // Attempt to load each plug-in if available.
    for (final DataType dataType : DataType.values()) {

      final Class<? extends SecurityData> pluginType = dataType.getValueClass();
      final String pluginName = dataType.getName();

      final String implementingClassPropertyValue;
      implementingClassPropertyValue =
            String.format("%s.plugin.classname", pluginName);

      final String implementingClassName;
      implementingClassName =
            properties.getProperty(implementingClassPropertyValue);

      if (implementingClassName == null) {
        LOG.debug("No '{}' plugin provider property {}", pluginName,
              implementingClassPropertyValue);
        continue;
      } else {
        LOG.debug("Checking for '{}' plugin provider property {}={}",
              pluginName, implementingClassPropertyValue,
              implementingClassName);
      }

      final Plugin<? extends SecurityData> plugin;
      plugin = loadPlugin(implementingClassName, pluginType);

      if (plugin != null) {
        plugins.put(pluginType, plugin);
        LOG.debug("Plugin provider loaded {}={}", pluginType,
              implementingClassName);
      } else {
        LOG.debug("No plugin provider loaded for '{}'", pluginType);
      }

    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Plugin<? extends SecurityData> loadPlugin(
        String implementingClassName,
        Class<? extends SecurityData> pluginType) {

    Plugin<? extends SecurityData> plugin = null;

    try {

      final Class<?> pluginClass = Class.forName(implementingClassName);
      plugin = (Plugin<? extends SecurityData>) pluginClass.newInstance();

      InvalidationReportHandlerImpl reportHandler;
      reportHandler =
            new InvalidationReportHandlerImpl(invalidationHandler, pluginType);
      plugin.registerInvalidationHandler(reportHandler);
      plugin.open(properties);

    } catch (final ReflectiveOperationException e) {
      LOG.warn("Can not create plugin : {}", implementingClassName, e);
    }

    return plugin;

  }

  @Override
  public void close() {
    for (final Plugin<? extends SecurityData> plugin : plugins.values()) {
      plugin.close();
    }
    plugins.clear();
  }

  @Override
  public Plugin<? extends SecurityData> getPlugin(DataType dataType) {
    return plugins.get(dataType.getValueClass());
  }

  @Override
  public Plugin<? extends SecurityData> getPlugin(
        Class<? extends SecurityData> clazz) {
    return plugins.get(clazz);
  }

  @Override
  public Collection<Plugin<? extends SecurityData>> getPlugins() {
    return plugins.values();
  }

}
