package dlord03.plugin.api.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * The base interface of all types of data that can be returned from a plug-in.
 * 
 * @author David Lord
 *
 */
public interface SecurityData extends Serializable {

  /**
   * The unique security identifier of this object.
   * 
   * @return the security identifier.
   */
  SecurityIdentifier getSecurityIdentifier();

  /**
   * The exact local date and time that this record was last updated.
   * 
   * @return the last updated date and time.
   */
  ZonedDateTime getUpdatedAt();
}
