package uk.ac.bbk.dlord03.cache.data;

import static uk.ac.bbk.dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

public class TemporalDataKeyImplTest {

  private SecurityIdentifier security;
  private Instant updatedTime;
  private LocalDate updatedDate;
  private static final String UPDATED_TIME = "2015-08-02T14:49:56.025Z";
  private static final String UPDATED_DATE = "2015-08-02";

  @Before
  public void setUp() {
    security = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_TIME).toInstant();
    updatedDate = LocalDate.parse(UPDATED_DATE);
  }

  @Test
  public void testTimestampFormatFromInstant() {
    TemporalKeyImpl key;
    key = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key = serializeRoundTrip(key);
    Assert.assertEquals(updatedTime, key.getTimestamp());
  }

  @Test
  public void testObjectEqualityFromInstant() {
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromInstant() {
    final ZonedDateTime dayBefore =
          ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security,
          dayBefore.toInstant());
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromInstant() {
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromInstant() {
    final ZonedDateTime dayBefore =
          ZonedDateTime.parse(UPDATED_TIME).minusDays(1);
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedTime);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security,
          dayBefore.toInstant());
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testObjectEqualityFromDate() {
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, dayBefore);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEqualityFromDate() {
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityFromDate() {
    final LocalDate dayBefore = updatedDate.minusDays(1);
    TemporalKeyImpl key1;
    key1 = new TemporalKeyImpl(DataType.DIVIDEND, security, updatedDate);
    TemporalKeyImpl key2;
    key2 = new TemporalKeyImpl(DataType.DIVIDEND, security, dayBefore);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
