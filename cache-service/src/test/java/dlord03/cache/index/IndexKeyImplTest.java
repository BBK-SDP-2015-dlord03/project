package dlord03.cache.index;

import static dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

public class IndexKeyImplTest {

  private SecurityIdentifier security;
  private SecurityIdentifier differentSecurity;

  @Before
  public void setUp() {
    security = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "VOD.L");
    differentSecurity =
      new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
  }

  @Test
  public void testObjectEquality() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnSecurity() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND,
      differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnDataType() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testObjectInequalityOnIndexType() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.INTRADAY, DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1, key2);
  }

  @Test
  public void testHashEquality() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertEquals(key1, key2);
  }

  @Test
  public void testHashInequalityOnSecurity() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND,
      differentSecurity);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityOnDataType() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashInequalityOnIndexType() {
    IndexKeyImpl key1;
    key1 = new IndexKeyImpl(IndexType.ENDOFDAY, DataType.DIVIDEND, security);
    IndexKeyImpl key2;
    key2 = new IndexKeyImpl(IndexType.LATEST, DataType.VOLATILITY, security);
    key1 = serializeRoundTrip(key1);
    key2 = serializeRoundTrip(key2);
    Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
  }

}
