package dlord03.cache;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleCacheKey implements Serializable {

  private static final long serialVersionUID = 1L;
  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final ZonedDateTime updatedAt;
  private final String toString;
  protected int hashCode;
  protected String timestamp;

  public SimpleCacheKey(CacheType cacheType, SecurityIdentifier securityIdentifier,
    ZonedDateTime updatedAt) {
    super();
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.updatedAt = updatedAt;
    this.timestamp = DateTimeFormatter.ISO_INSTANT.format(updatedAt);
    this.hashCode =
      31 * cacheType.hashCode() * securityIdentifier.hashCode() + updatedAt.hashCode();
    this.toString =
      String.format("SimpleCacheKey(cacheType=%s,securityIdentifier=%s,updatedAt=%s)",
        cacheType, securityIdentifier, timestamp);
  }

  public CacheType getCacheType() {
    return this.cacheType;
  }

  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  public String getTimestamp() {
    return timestamp;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return toString;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof SimpleCacheKey))
      return false;
    final SimpleCacheKey other = (SimpleCacheKey) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
      && this.updatedAt.equals(other.updatedAt)
      && this.cacheType.equals(other.cacheType));
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

}
