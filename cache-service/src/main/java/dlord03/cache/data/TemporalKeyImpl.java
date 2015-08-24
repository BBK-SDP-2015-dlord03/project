package dlord03.cache.data;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;

import dlord03.cache.support.TimeQueries;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class TemporalKeyImpl extends SimplKeyImpl implements TemporalKey {

  private static final long serialVersionUID = -8580381668516461833L;

  private final Long timestamp;

  public TemporalKeyImpl(DataType dataType, SecurityIdentifier security,
    TemporalAccessor timestamp) {
    super(dataType, security);
    this.timestamp = timestamp.query(TimeQueries.getTimestamp());
  }

  @Override
  public Instant getTimestamp() {
    return Instant.ofEpochMilli(timestamp);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + timestamp.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format(
      "TemporalKeyImpl(dataType=%s,securityIdentifier=%s,updatedAt=%s)",
      getDataType(), getSecurityIdentifier(), timestamp);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TemporalKeyImpl)) {
      return false;
    }
    final TemporalKeyImpl other = (TemporalKeyImpl) obj;
    return (super.equals(obj) && this.timestamp.equals(other.timestamp));
  }

}
