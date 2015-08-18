package dlord03.cache.service;

import java.time.Instant;
import java.time.LocalDate;
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
    Instant twoHoursAgo = Instant.now().minusSeconds(2 * 60 * 60);
    findIntraDayDividendRecord("BT.L", twoHoursAgo);
  }

  @Test(expected = java.lang.AssertionError.class)
  public void testGetMissingIntraDayDividendRecord() {
    Instant twoHoursAgo = Instant.now().minusSeconds(2 * 60 * 60);
    findIntraDayDividendRecord("SLET.L", twoHoursAgo);
  }

  @Test
  public void testGetEndOfDayDividendRecord() {
    LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
    findEndOfDayDividendRecord("BT.L", twoWeeksAgo);
  }

  @Test(expected = java.lang.AssertionError.class)
  public void testGetMissingEndOfDayDividendRecord() {
    LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
    findEndOfDayDividendRecord("SLET.L", twoWeeksAgo);
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
    Assert.assertEquals("BT Group Plc", optionContract.getName());
    Assert.assertEquals("Called plugin for cached value", 1, plugin.getLatestHitCount());

  }

  @Test
  public void testGetIntraDayOptionRecord() {
    Instant twoHoursAgo = Instant.now().minusSeconds(2 * 60 * 60);
    findIntraDayOptionRecord("BT.L", twoHoursAgo);
  }

  @Test(expected = java.lang.AssertionError.class)
  public void testGetMissingIntraDayOptionRecord() {
    Instant twoHoursAgo = Instant.now().minusSeconds(2 * 60 * 60);
    findIntraDayOptionRecord("SLET.L", twoHoursAgo);
  }

  @Test
  public void testGetEndOfDayOptionRecord() {
    LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
    findEndOfDayOptionRecord("BT.L", twoWeeksAgo);
  }

  @Test(expected = java.lang.AssertionError.class)
  public void testGetMissingEndOfDayOptionRecord() {
    LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
    findEndOfDayOptionRecord("SLET.L", twoWeeksAgo);
  }

  @SuppressWarnings("unchecked")
  private <T> T getLatestValue(DataType type, SecurityIdentifier security) {
    return (T) service.getLatestValue(type, security);
  }

  @SuppressWarnings("unchecked")
  private <T> T getLatestValue(DataType type, SecurityIdentifier security,
    Instant before) {
    return (T) service.getLatestValue(type, security, before);
  }

  @SuppressWarnings("unchecked")
  private <T> T getEndOfDayValue(DataType type, SecurityIdentifier security,
    LocalDate date) {
    return (T) service.getEndOfDayValue(type, security, date);
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

  private void findIntraDayDividendRecord(String ric, Instant asOf) {

    initialiseQueryService();

    // Get a reference to the dividend plug-in.
    DividendSchedulePluginImpl plugin = getDividendPlugin();

    // Construct a security predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, ric);

    // Search for the latest dividend record at time predicate.
    DividendSchedule dividends;
    dividends = getLatestValue(DataType.DIVIDEND, security, asOf);

    // Confirm the plug-in was called.
    long hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Failed to call plugin", 1, hitCount);

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", dividends);

    // Search again.
    dividends = null;
    dividends = getLatestValue(DataType.DIVIDEND, security, asOf);

    // Confirm the cached value was used and the plug-in was not called again.
    int dividendCount = 0;
    for (Dividend dividend : dividends) {
      Assert.assertNotNull(dividend.getAmount());
      dividendCount++;
    }
    Assert.assertTrue("No dividends", dividendCount > 0);

    hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Called plugin for cached value", 1, hitCount);

  }

  private void findEndOfDayDividendRecord(String ric, LocalDate date) {

    initialiseQueryService();

    // Get a reference to the dividend plug-in.
    DividendSchedulePluginImpl plugin = getDividendPlugin();

    // Construct a security predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, ric);

    // Search for the latest dividend record at time predicate.
    DividendSchedule dividends;
    dividends = getEndOfDayValue(DataType.DIVIDEND, security, date);

    // Confirm the plug-in was called.
    long hitCount = plugin.getEndOfDayHitCount();
    Assert.assertEquals("Failed to call plugin", 1, hitCount);

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", dividends);

    // Search again.
    dividends = null;
    dividends = getEndOfDayValue(DataType.DIVIDEND, security, date);

    // Confirm the cached value was used and the plug-in was not called again.
    int dividendCount = 0;
    for (Dividend dividend : dividends) {
      Assert.assertNotNull(dividend.getAmount());
      dividendCount++;
    }
    Assert.assertTrue("No dividends", dividendCount > 0);

    hitCount = plugin.getEndOfDayHitCount();
    Assert.assertEquals("Called plugin for cached value", 1, hitCount);

  }

  private void findIntraDayOptionRecord(String ric, Instant asOf) {

    initialiseQueryService();

    // Get a reference to the option plug-in.
    OptionContractPluginImpl plugin = getOptionPlugin();

    // Construct a security predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, ric);

    // Search for the latest dividend record at time predicate.
    OptionContract option;
    option = getLatestValue(DataType.OPTION, security, asOf);

    // Confirm the plug-in was called.
    long hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Failed to call plugin", 1, hitCount);

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", option);

    // Search again.
    option = null;
    option = getLatestValue(DataType.OPTION, security, asOf);

    // Confirm the cached value was used and the plug-in was not called again.
    Assert.assertNotNull("Failed to find record", option.getName());
    hitCount = plugin.getlatestPredicateHitCount();
    Assert.assertEquals("Called plugin for cached value", 1, hitCount);

  }

  private void findEndOfDayOptionRecord(String ric, LocalDate date) {

    initialiseQueryService();

    // Get a reference to the option plug-in.
    OptionContractPluginImpl plugin = getOptionPlugin();

    // Construct a security predicate.
    SecurityIdentifier security;
    security = new SecurityIdentifier(IdentifierScheme.RIC, ric);

    // Construct a intra-day predicate.
    LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

    // Search for the latest dividend record at time predicate.
    OptionContract option;
    option = getEndOfDayValue(DataType.OPTION, security, twoWeeksAgo);

    // Confirm the plug-in was called.
    long hitCount = plugin.getEndOfDayHitCount();
    Assert.assertEquals("Failed to call plugin", 1, hitCount);

    // Confirm the record was found.
    Assert.assertNotNull("Failed to find record", option);

    // Search again.
    option = null;
    option = getEndOfDayValue(DataType.OPTION, security, twoWeeksAgo);

    // Confirm the cached value was used and the plug-in was not called again.
    Assert.assertNotNull("Failed to find record", option.getName());

    hitCount = plugin.getEndOfDayHitCount();
    Assert.assertEquals("Called plugin for cached value", 1, hitCount);

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
