package uk.ac.bbk.dlord03.cache.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bbk.dlord03.cache.CacheController;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.SimpleKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.cache.index.Index;
import uk.ac.bbk.dlord03.cache.index.IndexImpl;
import uk.ac.bbk.dlord03.cache.index.IndexKey;
import uk.ac.bbk.dlord03.cache.index.IndexKeyImpl;
import uk.ac.bbk.dlord03.cache.index.IndexType;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.Instant;
import java.time.ZonedDateTime;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;

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
  public void tearDown() {
    cacheController.close();
    cacheManager.close();
  }

  @Test
  public void testModifyIndexCache() {

    final DataType dt = DataType.OPTION;
    final SecurityIdentifier si =
          new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    final IndexKey key = new IndexKeyImpl(IndexType.INTRADAY, dt, si);
    final Index index = new IndexImpl(dt, si);

    // Add an entry
    final Instant queryAge = ZonedDateTime.now().minusHours(2).toInstant();
    final ZonedDateTime dataAge = ZonedDateTime.now().minusHours(12);
    final TemporalKey storedKey =
          new TemporalKeyImpl(dt, si, dataAge.toInstant());
    // This record is 12 hours old but satisfied a query for 2 hour old data.
    index.addLatestKey(storedKey, queryAge);

    final Cache<IndexKey, Index> indexCache = cacheController.getIndexCache();
    indexCache.put(key, index);

    final IndexKey searchKey = new IndexKeyImpl(IndexType.INTRADAY, dt, si);
    final Index foundIndex = indexCache.get(searchKey);

    // The retrieved index is the same one we put in?
    Assert.assertTrue("Records not equal", foundIndex.equals(index));

    // The retrieved index is not literally the same object?
    Assert.assertFalse(index == foundIndex);

    // We can still find the entries.
    final Instant twoHoursAgo = ZonedDateTime.now().minusHours(2).toInstant();
    final Instant threeHoursAgo = ZonedDateTime.now().minusHours(3).toInstant();
    TemporalKey foundKey = foundIndex.getLatestKey(threeHoursAgo);
    Assert.assertTrue("Records not equal", foundKey.equals(storedKey));

    foundKey = foundIndex.getLatestKey(twoHoursAgo);
    Assert.assertNull("Found wrong record", foundKey);

  }

  @Test
  public void testCreateIndexCache() {
    final Cache<IndexKey, Index> cache = cacheController.getIndexCache();
    Assert.assertEquals("indexCache", cache.getName());
  }

  @Test
  public void testCreateLatestCache() {
    final Cache<SimpleKey, SecurityData> cache =
          cacheController.getLatestCache();
    Assert.assertEquals("latestCache", cache.getName());
  }

  @Test
  public void testCreateTimestampedCache() {
    final Cache<TemporalKey, SecurityData> cache =
          cacheController.getTimestampedCache();
    Assert.assertEquals("timestampedCache", cache.getName());
  }

  @Test
  public void testCreateDatedCache() {
    final Cache<TemporalKey, SecurityData> cache =
          cacheController.getDatedCache();
    Assert.assertEquals("datedCache", cache.getName());
  }

}
