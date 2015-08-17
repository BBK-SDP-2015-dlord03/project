package dlord03.cache.service;

import java.time.Instant;
import java.time.ZonedDateTime;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.CacheController;
import dlord03.cache.data.TemporalDataKey;
import dlord03.cache.data.TemporalDataKeyImpl;
import dlord03.cache.data.DataType;
import dlord03.cache.index.Index;
import dlord03.cache.index.IndexImpl;
import dlord03.cache.index.IndexKey;
import dlord03.cache.index.IndexKeyImpl;
import dlord03.cache.index.IndexType;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class CacheControllerImplTest {

  private CacheManager cacheManager;
  private CacheController cacheController;

  @Before
  public void setUp() {
    cacheManager = Caching.getCachingProvider().getCacheManager();
    cacheController = new CacheControllerImp(cacheManager);
    cacheController.open();
  }

  @After
  public void TearDown() {
    cacheController.close();
    cacheManager.close();
  }

  @Test
  public void createIndexCache() {

    DataType dt = DataType.OPTION;
    SecurityIdentifier si = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    IndexKey key = new IndexKeyImpl(IndexType.INTRADAY, dt, si);
    Index index = new IndexImpl(dt, si);

    // Add an entry
    Instant queryAge = ZonedDateTime.now().minusHours(2).toInstant();
    ZonedDateTime dataAge = ZonedDateTime.now().minusHours(12);
    TemporalDataKey storedKey = new TemporalDataKeyImpl(dt, si, dataAge.toInstant());
    // This record is 12 hours old but satisfied a query for 2 hour old data.
    index.addLatestKey(storedKey, queryAge);

    Cache<IndexKey, Index> indexCache = cacheController.getIndexCache();
    indexCache.put(key, index);

    IndexKey searchKey = new IndexKeyImpl(IndexType.INTRADAY, dt, si);
    Index foundIndex = indexCache.get(searchKey);

    // The retrieved index is the same one we put in?
    Assert.assertTrue("Records not equal", foundIndex.equals(index));

    // The retrieved index is not literally the same object?
    Assert.assertFalse(index == foundIndex);

    // We can still find the entries.
    Instant twoHoursAgo = ZonedDateTime.now().minusHours(2).toInstant();
    Instant threeHoursAgo = ZonedDateTime.now().minusHours(3).toInstant();
    TemporalDataKey foundKey = foundIndex.getLatestKey(threeHoursAgo);
    Assert.assertTrue("Records not equal", foundKey.equals(storedKey));

    foundKey = foundIndex.getLatestKey(twoHoursAgo);
    Assert.assertNull("Found wrong record", foundKey);

  }

  @Test
  public void verifyIndexCache() {
    Cache<IndexKey, Index> cache = cacheController.getIndexCache();
    Assert.assertEquals("indexCache", cache.getName());
  }

  @Test
  public void verifyLatestCache() {
    Cache<TemporalDataKey, SecurityData> cache = cacheController.getLatestCache();
    Assert.assertEquals("latestCache", cache.getName());
  }

  @Test
  public void verifyTimestampedCache() {
    Cache<TemporalDataKey, SecurityData> cache = cacheController.getTimestampedCache();
    Assert.assertEquals("timestampedCache", cache.getName());
  }

  @Test
  public void verifyDatedCache() {
    Cache<TemporalDataKey, SecurityData> cache = cacheController.getDatedCache();
    Assert.assertEquals("datedCache", cache.getName());
  }

}
