package dlord03.cache.data;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import dlord03.cache.TimeQueries;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DataKeyImpl implements DataKey {

  private static final long serialVersionUID = -8580381668516461833L;

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;
  private final Long timestamp;

  public DataKeyImpl(DataType dataType, SecurityIdentifier security, TemporalAccessor timestamp) {
    super();
    this.dataType = dataType;
    this.securityIdentifier = security;
    this.timestamp = timestamp.query(TimeQueries.getTimestamp());
  }

  @Override
  public DataType getDataType() {
    return this.dataType;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public Instant getTimestamp() {
    return Instant.ofEpochMilli(timestamp);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + dataType.hashCode();
    result = 31 * result + securityIdentifier.hashCode();
    result = 31 * result + timestamp.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "SimpleCacheKey(dataType=%s,securityIdentifier=%s,updatedAt=%s)", dataType,
        securityIdentifier, timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof DataKeyImpl)) return false;
    final DataKeyImpl other = (DataKeyImpl) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
        && this.timestamp.equals(other.timestamp)
        && this.dataType.equals(other.dataType));
  }

}
