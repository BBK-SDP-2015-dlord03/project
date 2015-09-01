package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.plugin.api.Plugin;

import java.io.Serializable;
import java.time.temporal.TemporalAccessor;

/**
 * An internal entry within an {@link IndexImpl} object used to store references
 * to a {@link TemporalKey} in the cache and the {@link TemporalAccessor} which
 * was used as a predicate in the query which retrieved it from the underlying
 * {@link Plugin} implementation.
 * 
 * @param <T> the type of predicate used for this index entry.
 *
 * @author David Lord
 * 
 */
public class IndexEntry<T extends TemporalAccessor>
      implements Comparable<IndexEntry<TemporalAccessor>>, Serializable {

  private static final long serialVersionUID = -258800001073888908L;
  private final TemporalKey dataKey;
  private final T predicate;

  public IndexEntry(TemporalKey key, T predicate) {
    super();
    this.dataKey = key;
    this.predicate = predicate;
  }

  public TemporalKey getKey() {
    return dataKey;
  }

  public T getPredicate() {
    return predicate;
  }

  @Override
  public int compareTo(IndexEntry<TemporalAccessor> other) {
    if (this == other) {
      return 0;
    }
    return this.dataKey.getTimestamp().compareTo(other.dataKey.getTimestamp());
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + dataKey.hashCode();
    result = 31 * result + predicate.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("IndexedCacheKey(key=%s,predicate=%s)",
          dataKey == null ? "null" : dataKey.toString(),
          predicate == null ? "null" : predicate.toString());
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof IndexEntry)) {
      return false;
    }
    final IndexEntry other = (IndexEntry) obj;
    return (this.dataKey.equals(other.dataKey)
          && this.predicate.equals(other.predicate));
  }

}
