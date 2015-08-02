package dlord03.cache;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DatedCacheKeyTests {


  private SecurityIdentifier security;
  private ZonedDateTime updatedTime;
  private LocalDate fixingDate;
  private final static String UPDATED_AT = "2015-08-02T14:49:56.025Z";
  private final static String FIXING_DATE = "2015-08-02";

  @Before
  public void setUp() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_AT);
    fixingDate = LocalDate.parse(FIXING_DATE);
  }

  @Test
  public void testTimestampFormat() {
    DatedCacheKey key = new DatedCacheKey(CacheType.DIVIDEND, security, updatedTime, fixingDate);
    Assert.assertEquals(FIXING_DATE, key.getTimestamp());
  }

  @Test
  public void testFixingDate() {
    DatedCacheKey key = new DatedCacheKey(CacheType.DIVIDEND, security, updatedTime, fixingDate);
    Assert.assertEquals(fixingDate, key.getFixingDate());
  }

}
