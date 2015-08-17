package dlord03.cache.data;

import java.io.Serializable;

import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * A simple data key for caching values from the plug-ins.
 * 
 * @author David Lord
 *
 */
public interface SimpleDataKey extends Serializable {

  /**
   * The data type of the value referred to by this key.
   * 
   * @return the data type.
   */
  DataType getDataType();

  /**
   * The security of the value referred to by this key.
   * 
   * @return the security.
   */
  SecurityIdentifier getSecurityIdentifier();

}
