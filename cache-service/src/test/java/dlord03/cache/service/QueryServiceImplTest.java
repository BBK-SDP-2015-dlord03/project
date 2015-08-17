package dlord03.cache.service;

import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.data.DataType;
import dlord03.cache.plugins.DividendSchedulePluginImpl;
import dlord03.plugin.api.data.Dividend;
import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class QueryServiceImplTest {

  private CacheManager cacheManager;
  Properties properties;
  QueryServiceImpl service;

  @Before
  public void setUp() {
    cacheManager = Caching.getCachingProvider().getCacheManager();
    properties = new Properties();
    service = new QueryServiceImpl();
  }

  @After
  public void tearDown() {
    service.stop();
  }

  @Test(expected = IllegalStateException.class)
  public void testServiceStartWithNoCache() {
    service.start();
  }

  @Test(expected = IllegalStateException.class)
  public void testServiceStartWithNoProperties() {
    service.setCacheManager(cacheManager);
    service.start();
  }

  @Test(expected = IllegalStateException.class)
  public void testServiceStartWithMissingPlugin() {
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    service.start();
  }

  @Test(expected = ClassCastException.class)
  public void testServiceStartWithInvalidPlugin() {
    properties.setProperty("option.plugin.classname",
      "dlord03.cache.plugins.InvalidPlugin");
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    service.start();
  }

  @Test
  public void testServiceStartWithValidPlugin() {
    properties.setProperty("option.plugin.classname",
      "dlord03.cache.plugins.OptionContractPluginImpl");
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    service.start();
  }

  @Test
  public void testGetLastestDividendRecord() {

    initialiseQueryService();

    // Get a reference to the dividend plug-in.
    DividendSchedulePluginImpl plugin;
    plugin = (DividendSchedulePluginImpl) service.getPluginController()
      .getPlugin(DataType.DIVIDEND);

    // Construct a predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    // Search for the latest dividend record.
    DividendSchedule dividends;
    dividends = (DividendSchedule) service.getLatestValue(DataType.DIVIDEND, security);

    // Confirm the plug-in was called.
    Assert.assertEquals("Failed to call plugin", 1, plugin.getLatestHitCount());

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", dividends);

    // Search again.
    dividends = null;
    dividends = (DividendSchedule) service.getLatestValue(DataType.DIVIDEND, security);

    // Confirm the cached value was used and the plug-in was not called again.
    Assert.assertNotNull("Failed to find record", dividends);
    Assert.assertEquals("Called plugin for cached value", 1, plugin.getLatestHitCount());

    int dividendCount = 0;
    for (Dividend dividend : dividends) {
      Assert.assertNotNull(dividend.getAmount());
      dividendCount++;
    }

    Assert.assertTrue("No dividends", dividendCount > 0);

  }

  private void initialiseQueryService() {
    properties.setProperty("option.plugin.classname",
      "dlord03.cache.plugins.OptionContractPluginImpl");
    properties.setProperty("dividend.plugin.classname",
      "dlord03.cache.plugins.DividendSchedulePluginImpl");
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    service.start();
  }

}
