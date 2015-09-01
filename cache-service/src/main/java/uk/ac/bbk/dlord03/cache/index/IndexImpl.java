package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Provides a index of cache keys allowing for them to be searched from one
 * record in the cache rather than needing to rely on the underlying cache's
 * implementation of a search which may involve querying many distributed nodes.
 * 
 * @author David Lord
 *
 */
public class IndexImpl implements Index {

  private static final long serialVersionUID = -8330314827556368663L;

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;
  private final NavigableSet<IndexEntry<Instant>> timestampedKeys;
  private final NavigableSet<IndexEntry<LocalDate>> datedKeys;

  public IndexImpl(DataType dataType, SecurityIdentifier securityIdentifier) {
    super();
    this.dataType = dataType;
    this.securityIdentifier = securityIdentifier;
    this.timestampedKeys = new ConcurrentSkipListSet<>();
    this.datedKeys = new ConcurrentSkipListSet<>();
  }

  public IndexImpl(Index index) {
    this(index.getDataType(), index.getSecurityIdentifier());
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public TemporalKey getLatestKey() {
    if (timestampedKeys.isEmpty())
      return null;
    return timestampedKeys.last().getKey();
  }

  @Override
  public TemporalKey getLatestKey(Instant before) {

    final TemporalKey predicateKey;
    predicateKey = new TemporalKeyImpl(dataType, securityIdentifier, before);

    final IndexEntry<Instant> predicate;
    predicate = new IndexEntry<>(predicateKey, before);

    IndexEntry<Instant> record;
    record = timestampedKeys.floor(predicate);
    if (record != null && record.getPredicate().compareTo(before) >= 0) {
      return record.getKey();
    }

    return null;

  }

  @Override
  public void addLatestKey(TemporalKey dataKey, Instant before) {
    validateKey(dataKey);
    final IndexEntry<Instant> foundKey = new IndexEntry<>(dataKey, before);
    timestampedKeys.add(foundKey);
  }

  @Override
  public TemporalKey getEndOfDayKey(LocalDate date) {

    final TemporalKey predicateKey;
    predicateKey = new TemporalKeyImpl(dataType, securityIdentifier, date);

    final IndexEntry<LocalDate> predicate;
    predicate = new IndexEntry<>(predicateKey, date);

    IndexEntry<LocalDate> record;
    record = datedKeys.floor(predicate);
    if (record != null && record.getPredicate().compareTo(date) >= 0) {
      return record.getKey();
    }

    return null;

  }

  @Override
  public void addEndOfDayKey(TemporalKey dataKey, LocalDate date) {
    validateKey(dataKey);
    final IndexEntry<LocalDate> foundKey = new IndexEntry<>(dataKey, date);
    datedKeys.add(foundKey);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + dataType.hashCode();
    result = 31 * result + securityIdentifier.hashCode();
    result = 31 * result + datedKeys.hashCode();
    result = 31 * result + timestampedKeys.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format(
          "IndexImpl(dataType=%s,securityIdentifier=%s,timestampedKeys=%s,datedKeys=%s)",
          getDataType(), getSecurityIdentifier(), timestampedKeys, datedKeys);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof IndexImpl)) {
      return false;
    }
    final IndexImpl other = (IndexImpl) obj;
    return (this.dataType.equals(other.dataType)
          && this.securityIdentifier.equals(other.securityIdentifier)
          && this.datedKeys.equals(other.datedKeys)
          && this.timestampedKeys.equals(other.timestampedKeys));
  }

  private void validateKey(TemporalKey dataKey) {
    if (!dataKey.getDataType().equals(dataType)) {
      throw new IllegalArgumentException();
    }
    if (!dataKey.getSecurityIdentifier().equals(securityIdentifier)) {
      throw new IllegalArgumentException();
    }
  }

}
