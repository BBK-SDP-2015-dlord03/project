package uk.ac.bbk.dlord03.plugin.api.data.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleSecurityIdentifierTest {

  private SecurityIdentifier vodafone1;
  private SecurityIdentifier vodafone2;
  private SecurityIdentifier britishTelecom1;
  private SecurityIdentifier britishTelecom2;
  private static final String RIC_VODAFONE = "VOD.L";
  private static final String RIC_BT = "BT.L";

  @Before
  public void setUp() {
    final IdentifierScheme scheme = IdentifierScheme.RIC;
    vodafone1 = new SimpleSecurityIdentifier(scheme, RIC_VODAFONE);
    vodafone2 = new SimpleSecurityIdentifier(scheme, RIC_VODAFONE);
    britishTelecom1 = new SimpleSecurityIdentifier(scheme, RIC_BT);
    britishTelecom2 = new SimpleSecurityIdentifier(scheme, RIC_BT);
  }

  @Test
  public void testEquality() {
    Assert.assertEquals(vodafone1, vodafone2);
    Assert.assertEquals(vodafone1.hashCode(), vodafone2.hashCode());
  }

  @Test
  public void testInequalityOnSymbol() {
    Assert.assertNotEquals(vodafone1, britishTelecom1);
  }

  @Test
  public void testInEqualityOnIdentifier() {
    final IdentifierScheme scheme1 = IdentifierScheme.ISIN;
    final String symbol = "GB0030913577";
    final SecurityIdentifier isinBt;
    isinBt = new SimpleSecurityIdentifier(scheme1, symbol);
    Assert.assertNotEquals(britishTelecom1, isinBt);

  }

  @Test
  public void testEqualToSelf() {
    Assert.assertEquals(britishTelecom1, britishTelecom1);
  }

  @Test
  public void testNotEqualToNull() {
    Assert.assertNotEquals(britishTelecom1, null);
  }

  @Test
  public void testNotEqualToWrongType() {
    Assert.assertNotEquals(britishTelecom1, "NotASecurityIdentifier");
  }

  @Test
  public void testToString() {
    Assert.assertEquals(britishTelecom1.toString(), "SecurityIdentifier(scheme=RIC,symbol=BT.L)");
  }

}
