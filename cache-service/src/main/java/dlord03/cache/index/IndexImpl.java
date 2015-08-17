package dlord03.cache.index;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import dlord03.cache.data.TemporalDataKey;
import dlord03.cache.data.TemporalDataKeyImpl;
import dlord03.cache.data.DataType;
import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * Provides a index of cache keys allowing for them to be searched from one record in the cache
 * rather than needing to rely on the underlying cache's implementation of a search which may
 * involve querying many distributed nodes.
 * 
 * @author David Lord
 *
 */
public class IndexImpl implements Index {

  private static final long serialVersionUID = -8330314827556368663L;

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;
  private final NavigableSet<IndexRecord<Instant>> timestampedKeys;
  private final NavigableSet<IndexRecord<LocalDate>> datedKeys;
  private TemporalDataKey latestKey = null;

  public IndexImpl(DataType dataType, SecurityIdentifier securityIdentifier) {
    super();
    this.dataType = dataType;
    this.securityIdentifier = securityIdentifier;
    this.timestampedKeys = new ConcurrentSkipListSet<>();
    this.datedKeys = new ConcurrentSkipListSet<>();
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
  public TemporalDataKey getLatestKey() {
    return latestKey;
  }

  @Override
  public TemporalDataKey getLatestKey(Instant before) {
    TemporalDataKey predicateKey = new TemporalDataKeyImpl(dataType, securityIdentifier, before);
    IndexRecord<Instant> predicate = new IndexRecord<>(predicateKey, before);
    IndexRecord<Instant> record;
    record = timestampedKeys.floor(predicate);
    if (record != null && record.getPredicate().isAfter(before)) return record.getKey();
    return null;
  }

  @Override
  public void addLatestKey(TemporalDataKey dataKey, Instant before) {
    validateKey(dataKey);
    IndexRecord<Instant> foundKey = new IndexRecord<>(dataKey, before);
    timestampedKeys.add(foundKey);
  }

  @Override
  public TemporalDataKey getEndOfDayKey(LocalDate date) {
    TemporalDataKey predicateKey = new TemporalDataKeyImpl(dataType, securityIdentifier, date);
    IndexRecord<LocalDate> predicate = new IndexRecord<>(predicateKey, date);
    IndexRecord<LocalDate> record;
    record = datedKeys.floor(predicate);
    if (record != null && record.getPredicate().isAfter(date)) return record.getKey();
    return null;
  }

  @Override
  public void addEndOfDayKey(TemporalDataKey dataKey, LocalDate date) {
    validateKey(dataKey);
    IndexRecord<LocalDate> foundKey = new IndexRecord<>(dataKey, date);
    datedKeys.add(foundKey);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + dataType.hashCode();
    result = 31 * result + securityIdentifier.hashCode();
    result = 31 * result + datedKeys.hashCode();
    result = 31 * result + timestampedKeys.hashCode();
    result = 31 * result + latestKey.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof IndexImpl)) return false;
    final IndexImpl other = (IndexImpl) obj;
    return (this.dataType.equals(other.dataType)
        && this.securityIdentifier.equals(other.securityIdentifier)
        && this.datedKeys.equals(other.datedKeys)
        && this.timestampedKeys.equals(other.timestampedKeys) && (latestKey == null
            ? other.latestKey == null
            : this.latestKey.equals(other.latestKey)));
  }

  private void validateKey(TemporalDataKey dataKey) {
    if (!dataKey.getDataType().equals(dataType)) throw new IllegalArgumentException();
    if (!dataKey.getSecurityIdentifier().equals(securityIdentifier))
      throw new IllegalArgumentException();
  }

}
