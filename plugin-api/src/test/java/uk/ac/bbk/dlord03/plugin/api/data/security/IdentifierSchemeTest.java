package uk.ac.bbk.dlord03.plugin.api.data.security;

import org.junit.Assert;
import org.junit.Test;

public class IdentifierSchemeTest {

  @Test
  public void createAndReadValues() {
    IdentifierScheme is =
          IdentifierScheme.valueOf(IdentifierScheme.RIC.toString());
    Assert.assertTrue(IdentifierScheme.RIC.equals(is));
  }

}
