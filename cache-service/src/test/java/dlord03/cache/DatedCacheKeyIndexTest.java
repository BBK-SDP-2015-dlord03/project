package dlord03.cache;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DatedCacheKeyIndexTest {

  private DatedCacheKeyIndex index;
  private CacheType cacheType;
  private SecurityIdentifier identifier;

  @Before
  public void setUp() {
    identifier = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    cacheType = CacheType.OPTION;
    index = new DatedCacheKeyIndex(cacheType, identifier);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkCacheType() {
    ZonedDateTime now = ZonedDateTime.now();
    LocalDate date = LocalDate.from(now);
    DatedCacheKey key;
    key = new DatedCacheKey(CacheType.DIVIDEND, identifier, date, now.minusHours(1));
    index.addEndOfDayKey(key, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkIdentifier() {
    SecurityIdentifier wrongIdentifier;
    wrongIdentifier = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    ZonedDateTime now = ZonedDateTime.now();
    LocalDate date = LocalDate.from(now);
    DatedCacheKey key;
    key = new DatedCacheKey(cacheType, wrongIdentifier, date, now.minusHours(1));
    index.addEndOfDayKey(key, date);
  }

  @Test
  public void refindEndOfDayKey() {
    ZonedDateTime now = ZonedDateTime.now();
    LocalDate date = LocalDate.from(now);
    LocalDate lastWeek = date.minusDays(7);
    DatedCacheKey keyIn;
    keyIn = new DatedCacheKey(cacheType, identifier, lastWeek, now.minusHours(1));
    index.addEndOfDayKey(keyIn, date);
    DatedCacheKey keyOut = index.getEndOfDayKey(date);
    Assert.assertEquals(keyIn, keyOut);
  }

}
