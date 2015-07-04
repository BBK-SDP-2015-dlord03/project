package dlord03.cache;

import java.io.Serializable;
import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Key extends Serializable {

  SecurityIdentifier getSecurityIdentifier();

  ZonedDateTime getUpdatedAt();

}
