package dlord03.cache;

import java.util.Collection;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;

/**
 * Central controller for configuring and accessing underlying plug-in implementations.
 * 
 * @author David Lord
 *
 */
public interface PluginController {

  void open();

  void close();

  Plugin<? extends SecurityData> getPlugin(DataType dataType);

  Collection<Plugin<? extends SecurityData>> getPlugins();

}
