package dlord03.cache.data;

import java.time.Instant;
import java.time.LocalDate;
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
  private LocalDate updatedDate;
  private final static String UPDATED_TIME = "2015-08-02T14:49:56.025Z";
  private final static String UPDATED_DATE = "2015-08-02";

  @Before
  public void setUp() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_TIME).toInstant();
    updatedDate = LocalDate.parse(UPDATED_DATE);
  }

  @Test
  public void testTimestampFormatFromInstant() {
    final DataKeyImpl key = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(updatedTime, key.getTimestamp());
  }

  @Test
  public void testObjectEqualityFromInstant() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromInstant() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromInstant() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromInstant() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testObjectEqualityFromDate() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromDate() {
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    final DataKeyImpl key1 = new DataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    final DataKeyImpl key2 = new DataKeyImpl(DataType.DIVIDEND, security, dayBefore);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
