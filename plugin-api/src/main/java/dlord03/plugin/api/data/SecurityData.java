package dlord03.plugin.api.data;

import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface SecurityData {

  SecurityIdentifier getSecurityIdentifier();

  ZonedDateTime getUpdatedAt();

}
