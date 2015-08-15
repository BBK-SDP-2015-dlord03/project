package dlord03.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

@RunWith(Parameterized.class)
public class SerialisationRoundTripTest {

  private static SecurityIdentifier security;
  private static ZonedDateTime updatedTime;
  private static LocalDate fixingDate;
  private Object key;
  private final static String UPDATED_AT = "2015-08-02T14:49:56.025Z";
  private final static String FIXING_DATE = "2015-08-02";

  @Parameters
  public static Collection<Object> data() {
    return Arrays.asList(new Object[] {createSimpleCacheKey(), createDatedCacheKey()});
  }

  public SerialisationRoundTripTest(Object key) {
    this.key = key;
  }

  @Test
  public void testRoundTripSerialisation() throws IOException, ClassNotFoundException {
    // Write the key object to a byte array.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(key);
    oos.close();
    baos.close();
    // Read the key object from the byte array.
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object roundTrippedObject = ois.readObject();
    ois.close();
    bais.close();
    // Verify that the read object is equal to the written one.
    Assert.assertEquals(key, roundTrippedObject);
  }

  private static SimpleCacheKey createSimpleCacheKey() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_AT);
    return new SimpleCacheKey(CacheType.DIVIDEND, security, updatedTime);
  }

  private static DatedCacheKey createDatedCacheKey() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    updatedTime = ZonedDateTime.parse(UPDATED_AT);
    fixingDate = LocalDate.parse(FIXING_DATE);
    return new DatedCacheKey(CacheType.DIVIDEND, security, fixingDate, updatedTime);
  }

}
