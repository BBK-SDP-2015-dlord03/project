package dlord03.cache.index;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.data.DataType;
import dlord03.cache.data.TemporalKey;
import dlord03.cache.data.TemporalKeyImpl;
import dlord03.cache.support.SerialisationUtils;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class IndexImplTest {

  private Index index;
  private DataType dataType;
  private SecurityIdentifier identifier;

  @Before
  public void setUp() {
    identifier = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    dataType = DataType.OPTION;
    index = new IndexImpl(dataType, identifier);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkCacheType() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl key = new TemporalKeyImpl(DataType.DIVIDEND,
      identifier, now.minusHours(1).toInstant());
    index.addLatestKey(key, now.toInstant());
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkIdentifier() {
    SecurityIdentifier wrongIdentifier;
    wrongIdentifier = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl key = new TemporalKeyImpl(dataType, wrongIdentifier,
      now.minusHours(1).toInstant());
    index.addLatestKey(key, now.toInstant());
  }

  @Test
  public void refindEndOfDayKey() {
    final ZonedDateTime now = ZonedDateTime.now();
    final LocalDate date = LocalDate.from(now);
    final Instant recordDate = now.minusDays(7).toInstant();
    TemporalKey keyIn;
    keyIn = new TemporalKeyImpl(dataType, identifier, recordDate);
    index.addEndOfDayKey(keyIn, date);
    final TemporalKey keyOut = index.getEndOfDayKey(date.minusDays(1));
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void refindLatestKey() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl keyIn =
      new TemporalKeyImpl(dataType, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(keyIn, now.toInstant());
    index = SerialisationUtils.serializeRoundTrip(index);
    final TemporalKey keyOut =
      index.getLatestKey(now.minusMinutes(10).toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

  @Test
  public void refindIntradayKey() {
    final ZonedDateTime now = ZonedDateTime.now();
    final TemporalKeyImpl keyIn =
      new TemporalKeyImpl(dataType, identifier, now.minusHours(1).toInstant());
    index.addLatestKey(keyIn, now.minusMinutes(10).toInstant());
    index = SerialisationUtils.serializeRoundTrip(index);
    final TemporalKey keyOut =
      index.getLatestKey(now.minusMinutes(20).toInstant());
    Assert.assertEquals(keyIn, keyOut);
  }

}
