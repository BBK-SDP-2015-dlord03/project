package dlord03.cache.service;

import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
  public void testRoutingRequest() {
    properties.setProperty("option.plugin.classname",
      "dlord03.cache.plugins.OptionContractPluginImpl");
    properties.setProperty("dividend.plugin.classname",
      "dlord03.cache.plugins.DividendSchedulePluginImpl");
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    service.start();

  }

}
