package dlord03.cache;

import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleCacheKeyTests {

  private SecurityIdentifier security;
  private ZonedDateTime updatedTime;
  private final static String UPDATED_AT = "2015-08-02T14:49:56.025Z";

  @Before
  public void setUp() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_AT);
  }

  @Test
  public void testTimestampFormat() {
    final SimpleCacheKey key = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(UPDATED_AT, key.getTimestamp());
  }

  @Test
  public void testObjectEquality() {
    final SimpleCacheKey key1 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    final SimpleCacheKey key2 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequality() {
    final ZonedDateTime dayBefore = updatedTime.minusDays(1);
    final SimpleCacheKey key1 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    final SimpleCacheKey key2 = new SimpleCacheKey(CacheType.DIVIDEND, security, dayBefore);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    final SimpleCacheKey key1 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    final SimpleCacheKey key2 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequality() {
    final ZonedDateTime dayBefore = updatedTime.minusDays(1);
    final SimpleCacheKey key1 = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    final SimpleCacheKey key2 = new SimpleCacheKey(CacheType.DIVIDEND, security, dayBefore);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
