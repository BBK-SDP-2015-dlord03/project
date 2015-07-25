package dlord03.cache;

import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

@SuppressWarnings("serial")
public class DateTimeCacheKey implements Key {

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final ZonedDateTime updatedAt;
  protected int hashCode;

  public DateTimeCacheKey(CacheType cacheType, SecurityIdentifier securityIdentifier,
      ZonedDateTime updatedAt) {
    super();
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.updatedAt = updatedAt;
    this.hashCode = 31 * securityIdentifier.hashCode() + updatedAt.hashCode();
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
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof DateTimeCacheKey)) return false;
    final DateTimeCacheKey other = (DateTimeCacheKey) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
        && this.updatedAt.equals(other.updatedAt) && this.cacheType.equals(other.cacheType));
  }

}
