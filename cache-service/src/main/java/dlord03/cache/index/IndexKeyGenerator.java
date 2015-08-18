package dlord03.cache.index;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class IndexKeyGenerator {

  public static IndexKey generate(IndexType indexType, DataType dataType,
    SecurityIdentifier securityIdentifier) {
    return new IndexKeyImpl(indexType, dataType, securityIdentifier);
  };

}
