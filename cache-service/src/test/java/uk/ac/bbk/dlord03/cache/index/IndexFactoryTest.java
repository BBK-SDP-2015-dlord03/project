package uk.ac.bbk.dlord03.cache.index;

import org.junit.Assert;
import org.junit.Test;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

public class IndexFactoryTest {

  @Test
  public void testCreateCopy() {

    DataType type;
    SecurityIdentifier id;
    id = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    type = DataType.OPTION;

    Index index1;
    index1 = new IndexImpl(type, id);

    Index index2 = IndexFactory.create(index1);

    Assert.assertEquals(index1, index2);

  }

}
