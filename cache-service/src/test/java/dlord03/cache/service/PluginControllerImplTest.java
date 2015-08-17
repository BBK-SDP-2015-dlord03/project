package dlord03.cache.service;

import static org.mockito.Mockito.mock;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.PluginController;

public class PluginControllerImplTest {

  PluginInvalidationReportHandler pluginInvalidationReportHandler;

  @Before
  public void setUp() {
    pluginInvalidationReportHandler = mock(PluginInvalidationReportHandler.class);
  }

  @Test
  public void testServiceStartWithNoProperties() {
    PluginController pluginController;
    pluginController = new PluginControllerImpl(null);
    pluginController.open();
    Assert.assertTrue(pluginController.getPlugins().size() == 0);
  }

  @Test(expected = ClassCastException.class)
  public void testServiceStartWithInvalidPlugin() {
    Properties properties = new Properties();
    properties.put("option.plugin.classname", "dlord03.cache.plugins.InvalidPlugin");
    properties.put("invalidationReportHandler", pluginInvalidationReportHandler);
    PluginController pluginController;
    pluginController = new PluginControllerImpl(properties);
    pluginController.open();
  }

  @Test
  public void testServiceStartWithValidPlugin() {
    Properties properties = new Properties();
    properties.put("option.plugin.classname",
      "dlord03.cache.plugins.OptionContractPluginImpl");
    properties.put("invalidationReportHandler", pluginInvalidationReportHandler);
    PluginController pluginController;
    pluginController = new PluginControllerImpl(properties);
    pluginController.open();
    Assert.assertEquals(1, pluginController.getPlugins().size());
  }

  @Test
  public void testServiceStartWithMultipleValidPlugins() {
    Properties properties = new Properties();
    properties.put("option.plugin.classname",
      "dlord03.cache.plugins.OptionContractPluginImpl");
    properties.put("dividend.plugin.classname",
      "dlord03.cache.plugins.DividendSchedulePluginImpl");
    properties.put("invalidationReportHandler", pluginInvalidationReportHandler);
    PluginController pluginController;
    pluginController = new PluginControllerImpl(properties);
    pluginController.open();
    Assert.assertEquals(2, pluginController.getPlugins().size());
  }

}
