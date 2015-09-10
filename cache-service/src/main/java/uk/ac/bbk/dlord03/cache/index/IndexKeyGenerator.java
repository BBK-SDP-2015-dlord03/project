package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

public class IndexKeyGenerator {

  public static IndexKey generate(IndexType indexType, DataType dataType,
        SecurityIdentifier securityIdentifier) {
    return new IndexKeyImpl(indexType, dataType, securityIdentifier);
  }

}
