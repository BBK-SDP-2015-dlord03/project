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
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnSecurity() {
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnDataType() {
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testHashInequalityOnSecurity() {
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityOnDataType() {
    SimpleDataKeyImpl key1;
    key1 = new SimpleDataKeyImpl(DataType.DIVIDEND, security);
    SimpleDataKeyImpl key2;
    key2 = new SimpleDataKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
