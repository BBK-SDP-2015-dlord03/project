package uk.ac.bbk.dlord03.cache.support;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

public class TimeQueries {

  private static final long MILLIS_IN_A_SECOND = 1000;
  private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

  /**
   * Private constructor since this is a utility class.
   */
  private TimeQueries() {}

  /**
   * Returns a {@link TemporalQuery} which compares two {@link TemporalAccessor} objects
   * with different precision.
   * 
   * @param other the other object to compare with.
   * @return 0, -1 or 1 if the other object is equal, less than, or equal respectively.
   * 
   */
  public static TemporalQuery<Integer> compareTo(TemporalAccessor other) {
    return new TemporalQuery<Integer>() {

      @Override
      public Integer queryFrom(TemporalAccessor temporal) {
        final long thisOne = getTimestamp(temporal);
        final long otherOne = getTimestamp(other);
        return Long.compare(thisOne, otherOne);
      }

    };

  }

  /**
   * Returns a {@link TemporalQuery} which extracts a @ {@code Long} timestamp from a
   * {@link TemporalAccessor} object which may not have the necessary precision.
   * 
   * @return
   */
  public static TemporalQuery<Long> getTimestamp() {
    return new TemporalQuery<Long>() {

      @Override
      public Long queryFrom(TemporalAccessor temporal) {
        return getTimestamp(temporal);
      }

    };

  }

  private static long getTimestamp(TemporalAccessor temporal) {

    final TemporalUnit precision = temporal.query(TemporalQueries.precision());
    long milliseconds = 0;

    if (precision == ChronoUnit.NANOS) {
      final long seconds = temporal.getLong(ChronoField.INSTANT_SECONDS);
      milliseconds = Math.multiplyExact(seconds, MILLIS_IN_A_SECOND);
      milliseconds += temporal.getLong(ChronoField.MILLI_OF_SECOND);
    } else if (precision == ChronoUnit.DAYS) {
      final long days = temporal.getLong(ChronoField.EPOCH_DAY);
      milliseconds = Math.multiplyExact(days, MILLIS_IN_A_DAY);
    }

    return milliseconds;

  }

}
