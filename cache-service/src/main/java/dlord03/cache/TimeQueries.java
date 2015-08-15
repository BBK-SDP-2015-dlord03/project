package dlord03.cache;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

public class TimeQueries {

  private final static long MILLIS_IN_A_SECOND = 1000;
  private final static long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

  public static TemporalQuery<Integer> compareTo(TemporalAccessor other) {
    return new TemporalQuery<Integer>() {

      @Override
      public Integer queryFrom(TemporalAccessor temporal) {
        long thisOne = getTimestamp(temporal);
        long otherOne = getTimestamp(other);
        return Long.compare(thisOne, otherOne);
      }

    };

  }

  public static TemporalQuery<Long> getTimestamp() {
    return new TemporalQuery<Long>() {

      @Override
      public Long queryFrom(TemporalAccessor temporal) {
        return getTimestamp(temporal);
      }

    };

  }

  private static long getTimestamp(TemporalAccessor temporal) {

    TemporalUnit precision = temporal.query(TemporalQueries.precision());
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
