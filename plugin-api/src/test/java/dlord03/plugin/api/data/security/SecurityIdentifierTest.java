package dlord03.plugin.api.data.security;

import org.junit.Assert;
import org.junit.Test;

public class SecurityIdentifierTest {

  @Test
  public void testEquality() {

    final IdentifierScheme scheme = IdentifierScheme.RIC;
    final String symbol = "BT.L";

    final SecurityIdentifier security1 =
      new SimpleSecurityIdentifier(scheme, symbol);
    final SecurityIdentifier security2 =
      new SimpleSecurityIdentifier(scheme, symbol);

    Assert.assertEquals(security1, security2);

  }

  @Test
  public void testInEqualityOnSymbol() {

    final IdentifierScheme scheme = IdentifierScheme.RIC;
    final String symbol1 = "BT.L";
    final String symbol2 = "VOD.L";

    final SecurityIdentifier security1 =
      new SimpleSecurityIdentifier(scheme, symbol1);
    final SecurityIdentifier security2 =
      new SimpleSecurityIdentifier(scheme, symbol2);

    Assert.assertNotEquals(security1, security2);

  }

  @Test
  public void testInEqualityOnIdentifier() {

    final IdentifierScheme scheme1 = IdentifierScheme.ISIN;
    final IdentifierScheme scheme2 = IdentifierScheme.RIC;
    final String symbol = "BT.L";

    final SecurityIdentifier security1 =
      new SimpleSecurityIdentifier(scheme1, symbol);
    final SecurityIdentifier security2 =
      new SimpleSecurityIdentifier(scheme2, symbol);

    Assert.assertNotEquals(security1, security2);

  }

}
