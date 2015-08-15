package dlord03.cache;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DatedCacheKey extends SimpleCacheKey {

  private final LocalDate fixingDate;
  private final int hashCode;
  private static final long serialVersionUID = 1L;

  public DatedCacheKey(CacheType cacheType, SecurityIdentifier securityIdentifier,
    LocalDate date, ZonedDateTime updatedAt) {
    super(cacheType, securityIdentifier, updatedAt);
    this.fixingDate = date;
    this.hashCode = this.hashCode + date.hashCode();
    this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE.format(updatedAt);
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof DatedCacheKey))
      return false;
    final DatedCacheKey other = (DatedCacheKey) obj;
    return (super.equals(obj) && this.fixingDate.equals(other.fixingDate));
  }

  public LocalDate getFixingDate() {
    return fixingDate;
  }

}
