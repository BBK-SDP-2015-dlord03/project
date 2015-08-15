package dlord03.cache;

import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.Before;
import org.junit.Test;

public class QueryServiceTest {

  private CacheManager cacheManager;
  Properties properties;
  QueryServiceImpl service;

  @Before
  public void setUp() {
    cacheManager = Caching.getCachingProvider().getCacheManager();
    properties = new Properties();
    service = new QueryServiceImpl();
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

}
