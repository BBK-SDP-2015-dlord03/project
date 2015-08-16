package dlord03.cache.data;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.data.DataType;
import dlord03.cache.data.DataKeyImpl;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DataKeyImplTest {

  private SecurityIdentifier security;
  private Instant updatedTime;
  private final static String UPDATED_AT = "2015-08-02T14:49:56.025Z";

  @Before
  public void setUp() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_AT).toInstant();
  }

  @Test
  public void testTimestampFormat() {
    final DataKeyImpl key = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(updatedTime, key.getTimestamp());
  }

  @Test
  public void testObjectEquality() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequality() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_AT).minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequality() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_AT).minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
