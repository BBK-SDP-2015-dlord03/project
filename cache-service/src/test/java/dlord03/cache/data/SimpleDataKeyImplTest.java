package dlord03.cache.data;

import static dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleDataKeyImplTest {

  private SecurityIdentifier security;
  private SecurityIdentifier differentSecurity;

  @Before
  public void setUp() {
    security = new SecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    differentSecurity = new SecurityIdentifier(IdentifierScheme.RIC, "BT.L");
  }

  @Test
  public void testObjectEquality() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnSecurity() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnDataType() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testHashInequalityOnSecurity() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityOnDataType() {
    SimplKeyImpl key1;
    key1 = new SimplKeyImpl(DataType.DIVIDEND, security);
    SimplKeyImpl key2;
    key2 = new SimplKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
