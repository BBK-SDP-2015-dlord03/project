package dlord03.cache.index;

import java.io.Serializable;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface IndexKey extends Serializable {

  IndexType getIndexType();
  
  DataType getDataType();

  SecurityIdentifier getsecurityIdentifier();

}
