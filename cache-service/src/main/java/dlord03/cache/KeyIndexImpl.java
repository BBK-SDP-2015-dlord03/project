package dlord03.cache;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * Provides a index of cache keys allowing for them to be searched from one record in the
 * cache rather than needing to rely on the underlying cache's implementation of a search
 * which may involve querying many distributed nodes.
 * 
 * @author David Lord
 *
 */
public class KeyIndexImpl implements KeyIndex {

  private static final long serialVersionUID = 1L;

  private final CacheType cacheType;
  private final SecurityIdentifier securityIdentifier;
  private final NavigableSet<IndexedCacheKey<Instant>> latestKeys;
  private final NavigableSet<IndexedCacheKey<LocalDate>> datedKeys;
  private Key latestKey = null;

  public KeyIndexImpl(CacheType cacheType, SecurityIdentifier securityIdentifier) {
    super();
    this.cacheType = cacheType;
    this.securityIdentifier = securityIdentifier;
    this.latestKeys = new ConcurrentSkipListSet<>();
    this.datedKeys = new ConcurrentSkipListSet<>();
  }

  @Override
  public CacheType getCacheType() {
    return cacheType;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public Key getLatestKey() {
    return latestKey;
  }

  @Override
  public Key getLatestKey(Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Key getEndOfDayKey(LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addLatestKey(Key key, Instant before) {
    validateKey(key);

  }

  @Override
  public void addEndOfDayKey(Key key, LocalDate date) {
    validateKey(key);

  }

  private void validateKey(Key key) {
    if (!key.getCacheType().equals(cacheType))
      throw new IllegalArgumentException();
    if (!key.getSecurityIdentifier().equals(securityIdentifier))
      throw new IllegalArgumentException();

  }

  /**
   * A key which holds information on another cache key together with the temporal
   * predicate which was used when initially querying for it.
   * 
   * @author David Lord
   * 
   */
  @SuppressWarnings("rawtypes")
  static class IndexedCacheKey<T extends TemporalAccessor>
    implements Comparable<IndexedCacheKey>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Key key;
    private final T predicate;
    private final Long timestamp;
    private final String toString;
    private final int hashCode;

    public IndexedCacheKey(Key key, T predicate) {
      super();
      this.key = key;
      this.predicate = predicate;
      this.timestamp = this.predicate.query(TimeQueries.getTimestamp());
      this.hashCode = 31 * key.hashCode() * predicate.hashCode();
      this.toString = String.format("IndexedCacheKey(key=%s,predicate=%s)",
        key.toString(), Long.toString(timestamp));
    }

    public Key getKey() {
      return key;
    }

    public T getPredicate() {
      return predicate;
    }

    @Override
    public int compareTo(IndexedCacheKey o) {
      if (this == o)
        return 0;
      return this.predicate.query(TimeQueries.compareTo(o.predicate));
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
      if (!(obj instanceof IndexedCacheKey))
        return false;
      final IndexedCacheKey other = (IndexedCacheKey) obj;
      return (this.key.equals(other.key) && this.predicate.equals(other.predicate));
    }

  }

}
