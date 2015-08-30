package uk.ac.bbk.dlord03.cache.index;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import org.junit.Assert;
import org.junit.Test;

public class KeyIndexTest {

  @Test
  public void testInstant() {
    final TemporalAccessor now = Instant.now();
    Assert.assertFalse("EPOCH_DAY is supported",
          now.isSupported(ChronoField.EPOCH_DAY));
    Assert.assertTrue("INSTANT_SECONDS not supported",
          now.isSupported(ChronoField.INSTANT_SECONDS));
    Assert.assertTrue("MILLI_OF_SECOND not supported",
          now.isSupported(ChronoField.MILLI_OF_SECOND));
  }

  @Test
  public void testMilliseconds() {
    final Instant instant = Instant.now();
    final TemporalAccessor temporal = instant;
    final Long seconds = temporal.getLong(ChronoField.INSTANT_SECONDS);
    long milliseconds = Math.multiplyExact(seconds, 1000);
    milliseconds += temporal.getLong(ChronoField.MILLI_OF_SECOND);
    Assert.assertEquals(instant.toEpochMilli(), milliseconds);
  }

  @Test
  public void testLocalDate() {
    final TemporalAccessor date = LocalDate.now();
    Assert.assertFalse("INSTANT_SECONDS is supported",
          date.isSupported(ChronoField.INSTANT_SECONDS));
    Assert.assertFalse("MILLI_OF_SECOND is supported",
          date.isSupported(ChronoField.MILLI_OF_SECOND));
    Assert.assertTrue("EPOCH_DAY not supported",
          date.isSupported(ChronoField.EPOCH_DAY));
  }

}
