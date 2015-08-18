package dlord03.cache.service;

import java.time.Instant;
import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import dlord03.cache.PluginController;
import dlord03.cache.data.DataType;
import dlord03.cache.index.IndexImpl;
import dlord03.cache.index.IndexKey;
import dlord03.cache.index.IndexKeyGenerator;
import dlord03.cache.index.IndexType;
import dlord03.cache.plugins.DividendSchedulePluginImpl;
import dlord03.cache.plugins.OptionContractPluginImpl;
import dlord03.plugin.api.data.Dividend;
import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.OptionContract;
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
    DividendSchedulePluginImpl plugin = getDividendPlugin();

    // Construct a predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    // Search for the latest dividend record.
    DividendSchedule dividends;
    dividends = getLatestValue(DataType.DIVIDEND, security);

    // Confirm the plug-in was called.
    Assert.assertEquals("Failed to call plugin", 1, plugin.getLatestHitCount());

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", dividends);

    // Search again.
    dividends = null;
    dividends = getLatestValue(DataType.DIVIDEND, security);

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

  @Test
  public void testGetIntraDayDividendRecord() {

  }

  @Test
  public void testGetEndOfDayDividendRecord() {

  }

  @Test
  public void testGetMissingIntraDayDividendRecord() {

  }

  @Test
  public void testGetMissingEndOfDayDividendRecord() {

  }

  @Test
  public void testGetLastestOptionRecord() {

    initialiseQueryService();

    // Get a reference to the dividend plug-in.
    OptionContractPluginImpl plugin;
    plugin = getOptionPlugin();

    // Construct a predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    // Search for the latest dividend record.
    OptionContract optionContract;
    optionContract = getLatestValue(DataType.OPTION, security);

    // Confirm the plug-in was called.
    Assert.assertEquals("Failed to call plugin", 1, plugin.getLatestHitCount());

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", optionContract);

    // Search again.
    optionContract = null;
    optionContract = getLatestValue(DataType.OPTION, security);

    // Confirm the cached value was used and the plug-in was not called again.
    Assert.assertNotNull("Failed to find record", optionContract.getName());
    Assert.assertEquals("Called plugin for cached value", 1, plugin.getLatestHitCount());

  }

  @Test
  public void testGetIntraDayOptionRecord() {

    initialiseQueryService();

    // Get a reference to the dividend plug-in.
    OptionContractPluginImpl plugin;
    plugin = getOptionPlugin();

    // Construct a security predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    // Construct a intra-day predicate.
    Instant twoHoursAgo = Instant.now().minusSeconds(2 * 60 * 60);
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    // Search for the latest dividend record at time predicate.
    OptionContract option;
    option = getLatestValue(DataType.OPTION, security, twoHoursAgo);

    // Confirm the plug-in was called.
    long hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Failed to call plugin", 1, hitCount);

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", option);

    // Search again.
    option = null;
    option = getLatestValue(DataType.OPTION, security, twoHoursAgo);

    // Confirm the cached value was used and the plug-in was not called again.
    Assert.assertNotNull("Failed to find record", option.getName());
    hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Called plugin for cached value", 1, hitCount);

  }

  @Test
  public void testGetEndOfDaOptionRecord() {

  }

  @Test
  public void testGetMissingIntraDayOptionRecord() {

  }

  @Test
  public void testGetMissingEndOfDayOptionRecord() {

  }

  @SuppressWarnings("unchecked")
  private <T> T getLatestValue(DataType type, SecurityIdentifier security,
    Instant before) {
    return (T) service.getLatestValue(type, security, before);
  }

  @SuppressWarnings("unchecked")
  private <T> T getLatestValue(DataType type, SecurityIdentifier security) {
    return (T) service.getLatestValue(type, security);
  }

  @Test
  public void verifyCopiesOfIndex() throws Exception {

    initialiseQueryService();

    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");

    IndexKey key;
    key = IndexKeyGenerator.generate(IndexType.ENDOFDAY, DataType.DIVIDEND, security);

    Object result1 = Whitebox.invokeMethod(service, "getIndex", key);
    Object result2 = Whitebox.invokeMethod(service, "getIndex", key);

    Assert.assertTrue(result1 instanceof IndexImpl);
    Assert.assertTrue(result2 instanceof IndexImpl);

    Assert.assertTrue(result1.equals(result2));
    Assert.assertFalse(result1 == result2);

  }

  private OptionContractPluginImpl getOptionPlugin() {
    OptionContractPluginImpl p;
    PluginController pc;
    pc = service.getPluginController();
    p = (OptionContractPluginImpl) pc.getPlugin(DataType.OPTION);
    return p;
  }

  private DividendSchedulePluginImpl getDividendPlugin() {
    DividendSchedulePluginImpl p;
    PluginController pc;
    pc = service.getPluginController();
    p = (DividendSchedulePluginImpl) pc.getPlugin(DataType.DIVIDEND);
    return p;
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
