package dlord03.cache;

import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleCacheKeyIndexTest {

  private SimpleCacheKeyIndex index;
  private CacheType cacheType;
  private SecurityIdentifier identifier;

  @Before
  public void setUp() {
    identifier = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    cacheType = CacheType.OPTION;
    index = new SimpleCacheKeyIndex(cacheType, identifier);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkCacheType() {
    ZonedDateTime now = ZonedDateTime.now();
    SimpleCacheKey key =
      new SimpleCacheKey(CacheType.DIVIDEND, identifier, now.minusHours(1));
    index.addLatestKey(key, now.toInstant());
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkIdentifier() {
    SecurityIdentifier wrongIdentifier;
    wrongIdentifier = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    ZonedDateTime now = ZonedDateTime.now();
    SimpleCacheKey key =
      new SimpleCacheKey(cacheType, wrongIdentifier, now.minusHours(1));
    index.addLatestKey(key, now.toInstant());
  }

  @Test
  public void refindLatestKey() {
    ZonedDateTime now = ZonedDateTime.now();
    SimpleCacheKey keyIn = new SimpleCacheKey(cacheType, identifier, now.minusHours(1));
    index.addLatestKey(keyIn, now.toInstant());
    index = SerialisationUtils.roundTrip(index);
    SimpleCacheKey keyOut = index.getLatestKey(now.toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void refindIntradayKey() {
    ZonedDateTime now = ZonedDateTime.now();
    SimpleCacheKey keyIn = new SimpleCacheKey(cacheType, identifier, now.minusHours(1));
    index.addLatestKey(keyIn, now.toInstant());
    index = SerialisationUtils.roundTrip(index);
    SimpleCacheKey keyOut = index.getLatestKey(now.toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

}
