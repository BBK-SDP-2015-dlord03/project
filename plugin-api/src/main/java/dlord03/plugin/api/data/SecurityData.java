package dlord03.plugin.api.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface SecurityData extends Serializable {

  SecurityIdentifier getSecurityIdentifier();

  ZonedDateTime getUpdatedAt();

}
