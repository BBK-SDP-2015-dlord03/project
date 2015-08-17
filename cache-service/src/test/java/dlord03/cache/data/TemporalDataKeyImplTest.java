package dlord03.cache.data;

import static dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class TemporalDataKeyImplTest {

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
    TemporalDataKeyImpl key;
    key = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key = serializeRoundTrip(key);
    Assert.assertEquals(updatedTime, key.getTimestamp());
  }

  @Test
  public void testObjectEqualityFromInstant() {
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromInstant() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromInstant() {
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromInstant() {
    final ZonedDateTime dayBefore = ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, dayBefore.toInstant());
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testObjectEqualityFromDate() {
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, dayBefore);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromDate() {
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    TemporalDataKeyImpl key1;
    key1 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalDataKeyImpl key2;
    key2 = new TemporalDataKeyImpl(DataType.DIVIDEND, security, dayBefore);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
