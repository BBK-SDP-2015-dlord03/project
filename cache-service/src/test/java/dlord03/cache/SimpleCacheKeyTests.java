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
    SimpleCacheKey key = new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(UPDATED_AT, key.getTimestamp());
  }

}
