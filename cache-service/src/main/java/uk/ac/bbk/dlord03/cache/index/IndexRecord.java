package uk.ac.bbk.dlord03.cache.index;

import java.io.Serializable;
import java.time.temporal.TemporalAccessor;

import uk.ac.bbk.dlord03.cache.data.TemporalKey;

/**
 * An object to index keys in the cache. It provides details of the key together
 * with the predicate that was used to find it.
 * 
 * @author David Lord
 * 
 */
public class IndexRecord<T extends TemporalAccessor>
      implements Comparable<IndexRecord<TemporalAccessor>>, Serializable {

  private static final long serialVersionUID = -258800001073888908L;
  private final TemporalKey dataKey;
  private final T predicate;

  public IndexRecord(TemporalKey dataKey, T predicate) {
    super();
    this.dataKey = dataKey;
    this.predicate = predicate;
  }

  public TemporalKey getKey() {
    return dataKey;
  }

  public T getPredicate() {
    return predicate;
  }

  @Override
  public int compareTo(IndexRecord<TemporalAccessor> other) {
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
    if (!(obj instanceof IndexRecord)) {
      return false;
    }
    final IndexRecord other = (IndexRecord) obj;
    return (this.dataKey.equals(other.dataKey)
          && this.predicate.equals(other.predicate));
  }

}