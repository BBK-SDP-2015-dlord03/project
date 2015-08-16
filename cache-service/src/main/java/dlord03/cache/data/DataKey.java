package dlord03.cache.data;

import java.io.Serializable;
import java.time.Instant;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface DataKey extends Serializable {

  DataType getDataType();

  SecurityIdentifier getSecurityIdentifier();

  Instant getTimestamp();

}
