package dlord03.cache;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleCacheKeyIndex implements KeyIndex {

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private SortedSet<Key> keys;

  public SimpleCacheKeyIndex(CacheType cacheType, SecurityIdentifier securityIdentifier) {
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.keys = new ConcurrentSkipListSet<>(getKeyComparator());
  }

  @Override
  public void add(Key key, TemporalAccessor asof) {
    if (!key.getCacheType().equals(cacheType)) throw new IllegalArgumentException();
    if (!key.getSecurityIdentifier().equals(securityIdentifier))
      throw new IllegalArgumentException();
    keys.add(key);
  }

  @Override
  public Key getLatestKey() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Key getLatestKeyAsOf(TemporalAccessor asof) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public CacheType getCacheType() {
    return cacheType;
  }
  
  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  protected Comparator<Key> getKeyComparator() {
    return new Comparator<Key>() {

      @Override
      public int compare(Key key1, Key key2) {
        return key1.getUpdatedAt().compareTo(key2.getUpdatedAt());
      }
    };
  }

}
