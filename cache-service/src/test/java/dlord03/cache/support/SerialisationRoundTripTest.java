package dlord03.cache.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dlord03.cache.data.DataType;
import dlord03.cache.data.TemporalKey;
import dlord03.cache.data.TemporalKeyImpl;
import dlord03.cache.index.IndexKey;
import dlord03.cache.index.IndexKeyImpl;
import dlord03.cache.index.IndexType;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

@RunWith(Parameterized.class)
public class SerialisationRoundTripTest {

  private final Object key;
  private final static String UPDATED_AT = "2015-08-02T14:49:56.025Z";

  @Parameters
  public static Collection<Object> data() {
    return Arrays.asList(new Object[] {createDataKey(), createIndexKey()});
  }

  public SerialisationRoundTripTest(Object key) {
    this.key = key;
  }

  @Test
  public void testRoundTripSerialisation()
    throws IOException, ClassNotFoundException {
    // Write the key object to a byte array.
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(key);
    oos.close();
    baos.close();
    // Read the key object from the byte array.
    final ByteArrayInputStream bais =
      new ByteArrayInputStream(baos.toByteArray());
    final ObjectInputStream ois = new ObjectInputStream(bais);
    final Object roundTrippedObject = ois.readObject();
    ois.close();
    bais.close();
    // Verify that the read object is equal to the written one.
    Assert.assertEquals(key, roundTrippedObject);
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
