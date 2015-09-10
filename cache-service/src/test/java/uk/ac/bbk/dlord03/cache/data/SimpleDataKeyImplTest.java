package uk.ac.bbk.dlord03.cache.data;

import static uk.ac.bbk.dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

public class SimpleDataKeyImplTest {

  private SecurityIdentifier security;
  private SecurityIdentifier differentSecurity;

  @Before
  public void setUp() {
    security = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    differentSecurity = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
  }

  @Test
  public void testObjectEquality() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnSecurity() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnDataType() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testHashInequalityOnSecurity() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.DIVIDEND, differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityOnDataType() {
    SimpleKeyImpl key1;
    key1 = new SimpleKeyImpl(DataType.DIVIDEND, security);
    SimpleKeyImpl key2;
    key2 = new SimpleKeyImpl(DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
