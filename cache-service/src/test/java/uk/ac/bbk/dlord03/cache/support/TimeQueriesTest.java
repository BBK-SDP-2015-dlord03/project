package uk.ac.bbk.dlord03.cache.support;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeQueriesTest {

  private final static String NOW = "2015-08-02T14:49:56.025Z";
  private final static String TODAY = "2015-08-02";

  private TemporalAccessor now;
  private TemporalAccessor justNow;
  private TemporalAccessor today;
  private TemporalAccessor yesterday;

  @Before
  public void setUp() {
    now = Instant.parse(NOW);
    justNow = Instant.parse(NOW).minusSeconds(60);
    today = LocalDate.parse(TODAY);
    yesterday = LocalDate.parse(TODAY).minusDays(1);
  }

  @Test
  public void instantToTimestamp() {
    final Long expected = Long.valueOf(1438526996025L);
    Assert.assertEquals("Wrong instant", expected,
          now.query(TimeQueries.getTimestamp()));
  }

  @Test
  public void localDateTimestamp() {
    final Long expected = Long.valueOf(1438473600000L);
    Assert.assertEquals("Wrong date", expected,
          today.query(TimeQueries.getTimestamp()));
  }

  @Test
  public void compareInstantsSame() {
    Assert.assertEquals(0, now.query(TimeQueries.compareTo(now)).intValue());
  }

  @Test
  public void compareInstantsGreaterThan() {
    Assert.assertEquals(1,
          now.query(TimeQueries.compareTo(justNow)).intValue());
  }

  @Test
  public void compareInstantsLessThan() {
    Assert.assertEquals(-1,
          justNow.query(TimeQueries.compareTo(now)).intValue());
  }

  @Test
  public void compareLocaDateSame() {
    Assert.assertEquals(0,
          today.query(TimeQueries.compareTo(today)).intValue());
  }

  @Test
  public void compareLocaDateGreaterThan() {
    Assert.assertEquals(-1,
          yesterday.query(TimeQueries.compareTo(today)).intValue());
  }

  @Test
  public void compareLocaDateLessThan() {
    Assert.assertEquals(1,
          today.query(TimeQueries.compareTo(yesterday)).intValue());
  }

  @Test
  public void crossCompareLessThan() {
    Assert.assertEquals(-1, today.query(TimeQueries.compareTo(now)).intValue());
  }

  @Test
  public void crossCompareGreaterThan() {
    Assert.assertEquals(1,
          justNow.query(TimeQueries.compareTo(today)).intValue());
  }

  /*
   * Some general tests to verify my understanding of the Java 8 date objects.
   */
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

  @Test
  public void testLocalDatePrecision() {
    final TemporalAccessor date = LocalDate.now();
    final TemporalUnit unit = date.query(TemporalQueries.precision());
    Assert.assertEquals(unit, ChronoUnit.DAYS);
  }

  @Test
  public void testInstantPrecision() {
    final TemporalAccessor instant = Instant.now();;
    final TemporalUnit unit = instant.query(TemporalQueries.precision());
    Assert.assertEquals(unit, ChronoUnit.NANOS);
  }

}
