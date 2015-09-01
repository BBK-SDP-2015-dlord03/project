package uk.ac.bbk.dlord03.cache.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.cache.index.IndexKey;
import uk.ac.bbk.dlord03.cache.index.IndexKeyImpl;
import uk.ac.bbk.dlord03.cache.index.IndexType;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SerialisationRoundTripTest {

  private static final String UPDATED_AT = "2015-08-02T14:49:56.025Z";
  private final Object key;

  @Parameters
  public static Collection<Object> data() {
    return Arrays.asList(new Object[] {createDataKey(), createIndexKey()});
  }

  public SerialisationRoundTripTest(Object key) {
    this.key = key;
  }

  @Test
  public void testRoundTrip() {
    final Object roundTrippedObject =
          SerialisationUtils.serializeRoundTrip(key);
    Assert.assertEquals(key, roundTrippedObject);
  }

  private static TemporalKey createDataKey() {
    final SecurityIdentifier security =
          new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    final ZonedDateTime updatedTime = ZonedDateTime.parse(UPDATED_AT);
    return new TemporalKeyImpl(DataType.DIVIDEND, security,
          updatedTime.toInstant());
  }

  private static IndexKey createIndexKey() {
    final SecurityIdentifier security =
          new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    final IndexType indexType = IndexType.INTRADAY;
    final DataType dataType = DataType.DIVIDEND;
    return new IndexKeyImpl(indexType, dataType, security);
  }

}
