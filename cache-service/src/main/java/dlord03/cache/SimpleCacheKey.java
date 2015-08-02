package dlord03.cache;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import dlord03.plugin.api.data.security.SecurityIdentifier;

@SuppressWarnings("serial")
public class SimpleCacheKey implements Key {

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final ZonedDateTime updatedAt;
  protected int hashCode;
  protected String timestamp;
  
  public SimpleCacheKey(CacheType cacheType, SecurityIdentifier securityIdentifier,
      ZonedDateTime updatedAt) {
    super();
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.updatedAt = updatedAt;
    this.hashCode = 31 * securityIdentifier.hashCode() + updatedAt.hashCode();
    this.timestamp = DateTimeFormatter.ISO_INSTANT.format(updatedAt);
  }

  @Override
  public CacheType getCacheType() {
    return this.cacheType;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public String getTimestamp() {
    return timestamp;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof SimpleCacheKey)) return false;
    final SimpleCacheKey other = (SimpleCacheKey) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
        && this.updatedAt.equals(other.updatedAt) && this.cacheType.equals(other.cacheType));
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }
  
  

}
