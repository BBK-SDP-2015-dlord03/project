package dlord03.cache;

import java.io.Serializable;
import java.time.Instant;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Key extends Serializable {

  CacheType getCacheType();

  SecurityIdentifier getSecurityIdentifier();

  Instant getTimestamp();

}
