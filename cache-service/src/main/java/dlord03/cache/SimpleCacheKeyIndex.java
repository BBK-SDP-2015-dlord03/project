package dlord03.cache;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleCacheKeyIndex implements Serializable {

  private static final long serialVersionUID = 1L;

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final SortedSet<SimpleCacheKey> keys;
  private final Comparator<SimpleCacheKey> comparator;

  public SimpleCacheKeyIndex(CacheType cacheType, SecurityIdentifier securityIdentifier) {
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.comparator = new KeyComparator();
    this.keys = new ConcurrentSkipListSet<>(comparator);
  }

  public CacheType getCacheType() {
    return cacheType;
  }

  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  public void addLatestKey(SimpleCacheKey key, Instant before) {
    if (!key.getCacheType().equals(cacheType))
      throw new IllegalArgumentException();
    if (!key.getSecurityIdentifier().equals(securityIdentifier))
      throw new IllegalArgumentException();
    keys.add((SimpleCacheKey) key);
  }

  public SimpleCacheKey getLatestKey() {
    return keys.last();
  }

  public SimpleCacheKey getLatestKey(Instant before) {
    ZonedDateTime localBefore = before.atZone(ZoneId.systemDefault());
    return keys.stream().filter(p -> p.getUpdatedAt().isBefore(localBefore))
      .max(comparator).orElse(null);
  }

  static class KeyComparator implements Comparator<SimpleCacheKey>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(SimpleCacheKey key1, SimpleCacheKey key2) {
      return key1.getUpdatedAt().compareTo(key2.getUpdatedAt());
    }

  }

}
