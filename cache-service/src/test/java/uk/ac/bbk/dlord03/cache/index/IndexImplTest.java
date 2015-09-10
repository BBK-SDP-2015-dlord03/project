package uk.ac.bbk.dlord03.cache.index;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.cache.support.SerialisationUtils;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class IndexImplTest {

  private IndexImpl index;
  private DataType dataType;
  private SecurityIdentifier identifier;

  @Before
  public void setUp() {
    identifier = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    dataType = DataType.OPTION;
    index = new IndexImpl(dataType, identifier);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCacheType() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl key =
          new TemporalKeyImpl(DataType.DIVIDEND, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(key, now.toInstant());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIdentifier() {
    SecurityIdentifier wrongIdentifier;
    wrongIdentifier = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl key =
          new TemporalKeyImpl(dataType, wrongIdentifier, now.minusHours(1).toInstant());
    index.addLatestKey(key, now.toInstant());
  }

  @Test
  public void testCreateFromAnother() {
    Index newIndex = new IndexImpl(index);
    Assert.assertEquals(index, newIndex);
  }

  @Test
  public void testGetSecurityIdentifier() {
    Assert.assertEquals(identifier, index.getSecurityIdentifier());
  }

  @Test
  public void testGetDataType() {
    Assert.assertEquals(dataType, index.getDataType());
  }

  @Test
  public void testRefindEndOfDayKey() {

    // Create required variables.
    final LocalDate today = LocalDate.from(ZonedDateTime.now());
    final Instant weekAgo = ZonedDateTime.now().minusDays(7).toInstant();

    // Create a week old key.
    TemporalKey keyIn = new TemporalKeyImpl(dataType, identifier, weekAgo);

    // Add this key to the index with a predicate of today.
    index.addEndOfDayKey(keyIn, today);

    // Get the latest key as of yesterday.
    final TemporalKey keyOut = index.getEndOfDayKey(today.minusDays(1));

    // Assert that the index found the same key.
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void testUpdateNewerEndOfDayKey() {

    // Create required variables.
    final LocalDate today = LocalDate.from(ZonedDateTime.now());
    final LocalDate yesterday = today.minusDays(1);
    final Instant oneWeekAgo = ZonedDateTime.now().minusDays(7).toInstant();

    // Create a week old key.
    TemporalKey key = new TemporalKeyImpl(dataType, identifier, oneWeekAgo);

    // Confirm the index is empty.
    Assert.assertTrue(index.datedKeys.size() == 0);

    // Add this key to the index with a predicate of yesterday.
    index.addEndOfDayKey(key, yesterday);

    // Confirm the key was added to the index.
    Assert.assertTrue(index.datedKeys.size() == 1);

    // Attempt an update with an newer predicate.
    index.addEndOfDayKey(key, today);

    // Confirm the newer key replaced the existing one.
    Assert.assertTrue(index.datedKeys.size() == 1);
    Assert.assertTrue(index.datedKeys.first().getPredicate().equals(today));

  }

  @Test
  public void testIgnoreOlderEndOfDayKey() {

    // Create required variables.
    final LocalDate today = LocalDate.from(ZonedDateTime.now());
    final LocalDate yesterday = today.minusDays(1);
    final Instant oneWeekAgo = ZonedDateTime.now().minusDays(7).toInstant();

    // Create a week old key.
    TemporalKey key = new TemporalKeyImpl(dataType, identifier, oneWeekAgo);

    // Confirm the index is empty.
    Assert.assertTrue(index.datedKeys.size() == 0);

    // Add this key to the index with a predicate of today.
    index.addEndOfDayKey(key, today);

    // Confirm the key was added to the index.
    Assert.assertTrue(index.datedKeys.size() == 1);

    // Attempt an update with an older predicate.
    index.addEndOfDayKey(key, yesterday);

    // Confirm the older key did not replace the existing one.
    Assert.assertTrue(index.datedKeys.size() == 1);
    Assert.assertTrue(index.datedKeys.first().getPredicate().equals(today));

  }

  @Test
  public void testUpdateNewerIntraDayKey() {

    // Create required variables.
    final ZonedDateTime now = ZonedDateTime.now();
    final Instant oneHourAgo = now.minusHours(1).toInstant();
    final Instant twoHoursAgo = now.minusHours(2).toInstant();
    final Instant oneWeekAgo = ZonedDateTime.now().minusDays(7).toInstant();

    // Create a week old key.
    TemporalKey key = new TemporalKeyImpl(dataType, identifier, oneWeekAgo);

    // Confirm the index is empty.
    Assert.assertTrue(index.timestampedKeys.size() == 0);

    // Add this key to the index with a predicate of two hours ago.
    index.addLatestKey(key, twoHoursAgo);

    // Confirm the key was added to the index.
    Assert.assertTrue(index.timestampedKeys.size() == 1);

    // Attempt an update with an newer predicate.
    index.addLatestKey(key, oneHourAgo);

    // Confirm the newer key replaced the existing one.
    Assert.assertTrue(index.timestampedKeys.size() == 1);
    Assert.assertTrue(index.timestampedKeys.first().getPredicate().equals(oneHourAgo));
  }

  @Test
  public void testIgnoreOlderIntraDayKey() {

    // Create required variables.
    final ZonedDateTime now = ZonedDateTime.now();
    final Instant oneHourAgo = now.minusHours(1).toInstant();
    final Instant twoHoursAgo = now.minusHours(2).toInstant();
    final Instant oneWeekAgo = ZonedDateTime.now().minusDays(7).toInstant();

    // Create a week old key.
    TemporalKey key = new TemporalKeyImpl(dataType, identifier, oneWeekAgo);

    // Confirm the index is empty.
    Assert.assertTrue(index.timestampedKeys.size() == 0);

    // Add this key to the index with a predicate of one hour ago.
    index.addLatestKey(key, oneHourAgo);

    // Confirm the key was added to the index.
    Assert.assertTrue(index.timestampedKeys.size() == 1);

    // Attempt an update with an older predicate.
    index.addLatestKey(key, twoHoursAgo);

    // Confirm the older key did not replace the existing one.
    Assert.assertTrue(index.timestampedKeys.size() == 1);
    Assert.assertTrue(index.timestampedKeys.first().getPredicate().equals(oneHourAgo));

  }

  @Test
  public void testRefindLatestKey() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl keyIn =
          new TemporalKeyImpl(dataType, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(keyIn, now.toInstant());
    index = SerialisationUtils.serializeRoundTrip(index);
    final TemporalKey keyOut = index.getLatestKey(now.minusMinutes(10).toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void testRefindIntradayKey() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl keyIn =
          new TemporalKeyImpl(dataType, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(keyIn, now.minusMinutes(10).toInstant());
    index = SerialisationUtils.serializeRoundTrip(index);
    final TemporalKey keyOut = index.getLatestKey(now.minusMinutes(20).toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void testEquals() {
    Assert.assertNull(index.getLatestKey());
    Index otherIndex = new IndexImpl(dataType, identifier);
    Assert.assertEquals(index, otherIndex);
  }

  @Test
  public void testEqualLatest() {

    // Create required variables.
    final Instant weekAgo = ZonedDateTime.now().minusDays(7).toInstant();
    final Instant now = Instant.now();

    // Create a week old key.
    TemporalKey key = new TemporalKeyImpl(dataType, identifier, weekAgo);

    index.addLatestKey(key, now);
    Assert.assertNotNull(index.getLatestKey());

    Index otherIndex = new IndexImpl(dataType, identifier);
    otherIndex.addLatestKey(key, now);
    Assert.assertNotNull(otherIndex.getLatestKey());

    Assert.assertEquals(index, otherIndex);

  }

  @Test
  public void testNotEqualLatest() {

    // Create a day old key.
    final Instant dayAgo = ZonedDateTime.now().minusDays(1).toInstant();
    TemporalKey dayOldKey = new TemporalKeyImpl(dataType, identifier, dayAgo);

    // Create a week old key.
    final Instant weekAgo = ZonedDateTime.now().minusDays(7).toInstant();
    TemporalKey weekOldKey = new TemporalKeyImpl(dataType, identifier, weekAgo);

    index.addLatestKey(dayOldKey, Instant.now());
    Assert.assertNotNull(index.getLatestKey());

    Index otherIndex = new IndexImpl(dataType, identifier);
    otherIndex.addLatestKey(weekOldKey, Instant.now().minusSeconds(10));
    Assert.assertNotNull(otherIndex.getLatestKey());

    Assert.assertNotEquals(index, otherIndex);

  }

  @Test
  public void testEqualHashCode() {
    Assert.assertNull(index.getLatestKey());
    Index otherIndex = new IndexImpl(dataType, identifier);
    Assert.assertEquals(index.hashCode(), otherIndex.hashCode());
  }

  @Test
  public void testEqualToString() {
    Assert.assertNull(index.getLatestKey());
    Index otherIndex = new IndexImpl(dataType, identifier);
    Assert.assertEquals(index.toString(), otherIndex.toString());
  }

  @Test
  public void testNotEqualsDifferentType() {
    Object otherIndex = new Object();
    Assert.assertNotEquals(index, otherIndex);
  }

  @Test
  public void testNotEquals() {
    Assert.assertNull(index.getLatestKey());
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl keyIn =
          new TemporalKeyImpl(dataType, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(keyIn, now.minusMinutes(10).toInstant());
    Index otherIndex = new IndexImpl(dataType, identifier);
    Assert.assertNotEquals(index, otherIndex);
  }

}
