package uk.ac.bbk.dlord03.cache.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Provides a index of cache keys allowing for them to be searched from one record in the
 * cache rather than needing to rely on the underlying cache's implementation of a search
 * which may involve querying many distributed nodes.
 * 
 * @author David Lord
 *
 */
public class IndexImpl implements Index {

  private static final long serialVersionUID = -8330314827556368663L;

  private static final Logger LOG = LoggerFactory.getLogger(IndexImpl.class);

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;
  final NavigableSet<IndexEntry<Instant>> timestampedKeys;
  final NavigableSet<IndexEntry<LocalDate>> datedKeys;

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
    if (timestampedKeys.isEmpty()) {
      return null;
    }
    return timestampedKeys.last().getKey();
  }

  @Override
  public TemporalKey getLatestKey(Instant predicate) {

    final TemporalKey key;
    key = new TemporalKeyImpl(dataType, securityIdentifier, predicate);

    final IndexEntry<Instant> predicateKey;
    predicateKey = new IndexEntry<>(key, predicate);

    IndexEntry<Instant> record;
    record = timestampedKeys.floor(predicateKey);
    if (record != null && record.getPredicate().compareTo(predicate) >= 0) {
      return record.getKey();
    }

    return null;

  }

  private void validateKey(TemporalKey dataKey) {
    if (!dataKey.getDataType().equals(dataType)) {
      throw new IllegalArgumentException();
    }
    if (!dataKey.getSecurityIdentifier().equals(securityIdentifier)) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public TemporalKey getEndOfDayKey(LocalDate predicate) {

    final TemporalKey key;
    key = new TemporalKeyImpl(dataType, securityIdentifier, predicate);

    final IndexEntry<LocalDate> predicateKey;
    predicateKey = new IndexEntry<>(key, predicate);

    IndexEntry<LocalDate> record;
    record = datedKeys.floor(predicateKey);
    if (record != null && record.getPredicate().compareTo(predicate) >= 0) {
      return record.getKey();
    }

    return null;

  }

  @Override
  public void addLatestKey(TemporalKey key, Instant predicate) {
    validateKey(key);
    final IndexEntry<Instant> foundKey = new IndexEntry<>(key, predicate);
    final IndexEntry<Instant> existingKey;
    existingKey = timestampedKeys.floor(foundKey);
    if (existingKey == null || existingKey.getPredicate().compareTo(predicate) < 0) {
      if (existingKey != null) {
        timestampedKeys.remove(existingKey);
      }
      if (timestampedKeys.add(foundKey)) {
        LOG.debug("Intraday index updated for {}", key.getSecurityIdentifier().getSymbol());
      }
    }
  }

  @Override
  public void addEndOfDayKey(TemporalKey key, LocalDate predicate) {
    validateKey(key);
    final IndexEntry<LocalDate> foundKey = new IndexEntry<>(key, predicate);
    final IndexEntry<LocalDate> existingKey;
    existingKey = datedKeys.floor(foundKey);
    if (existingKey == null || existingKey.getPredicate().compareTo(predicate) < 0) {
      if (existingKey != null) {
        datedKeys.remove(existingKey);
      }
      if (datedKeys.add(foundKey)) {
        LOG.debug("End of day index updated for {}", key.getSecurityIdentifier().getSymbol());
      }
    }
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

}
