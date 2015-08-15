package dlord03.cache;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class DatedCacheKeyIndex implements Serializable {

  private static final long serialVersionUID = 1L;

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final SortedSet<DatedCacheKey> keys;

  public DatedCacheKeyIndex(CacheType cacheType, SecurityIdentifier securityIdentifier) {
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.keys = new ConcurrentSkipListSet<>(getKeyComparator());
  }

  public CacheType getCacheType() {
    return cacheType;
  }

  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  public DatedCacheKey getEndOfDayKey(LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  public void addEndOfDayKey(DatedCacheKey key, LocalDate date) {
    if (!key.getCacheType().equals(cacheType))
      throw new IllegalArgumentException();
    if (!key.getSecurityIdentifier().equals(securityIdentifier))
      throw new IllegalArgumentException();
    keys.add(key);
  }

  protected Comparator<DatedCacheKey> getKeyComparator() {
    return new Comparator<DatedCacheKey>() {

      @Override
      public int compare(DatedCacheKey key1, DatedCacheKey key2) {
        return key1.getFixingDate().compareTo(key2.getFixingDate());
      }
    };
  }

}
