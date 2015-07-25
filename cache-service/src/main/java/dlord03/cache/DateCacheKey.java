package dlord03.cache;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

@SuppressWarnings("serial")
public class DateCacheKey extends DateTimeCacheKey {

  private final LocalDate fixingDate;
  private final int hashCode;

  public DateCacheKey(CacheType cacheType, SecurityIdentifier securityIdentifier,
      ZonedDateTime updatedAt, LocalDate fixingDate) {
    super(cacheType, securityIdentifier, updatedAt);
    this.fixingDate = fixingDate;
    this.hashCode = this.hashCode + fixingDate.hashCode();
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) return true;
    if (!(obj instanceof DateCacheKey)) return false;
    final DateCacheKey other = (DateCacheKey) obj;
    return (super.equals(obj) && this.fixingDate.equals(other.fixingDate));
  }

}