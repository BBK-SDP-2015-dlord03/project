package dlord03.plugin.api.data.security;

import org.junit.Assert;
import org.junit.Test;

public class SecurityIdentifierTest {

  @Test
  public void testEquality() {

    IdentifierScheme scheme = IdentifierScheme.RIC;
    String symbol = "BT.L";

    SecurityIdentifier security1 = new SecurityIdentifier(scheme, symbol);
    SecurityIdentifier security2 = new SecurityIdentifier(scheme, symbol);

    Assert.assertEquals(security1, security2);

  }

  @Test
  public void testInEqualityOnSymbol() {

    IdentifierScheme scheme = IdentifierScheme.RIC;
    String symbol1 = "BT.L";
    String symbol2 = "VOD.L";

    SecurityIdentifier security1 = new SecurityIdentifier(scheme, symbol1);
    SecurityIdentifier security2 = new SecurityIdentifier(scheme, symbol2);

    Assert.assertNotEquals(security1, security2);

  }

  @Test
  public void testInEqualityOnIdentifier() {

    IdentifierScheme scheme1 = IdentifierScheme.ISIN;
    IdentifierScheme scheme2 = IdentifierScheme.RIC;
    String symbol = "BT.L";

    SecurityIdentifier security1 = new SecurityIdentifier(scheme1, symbol);
    SecurityIdentifier security2 = new SecurityIdentifier(scheme2, symbol);

    Assert.assertNotEquals(security1, security2);

  }

}
