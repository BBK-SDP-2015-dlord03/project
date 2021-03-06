package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * An interface for classes which provide an index of {@link TemporalKey} objects. An
 * index is unique for a combination of {@link DataType} and {@link SecurityIdentifier}.
 * 
 * @author David Lord
 *
 */
public interface Index extends Serializable {

  /**
   * The {@link DataType} of the keys in this index.
   * 
   * @return the data type of this index.
   */
  DataType getDataType();

  /**
   * The {@link SecurityIdentifier} of the keys in this index.
   * 
   * @return the security identifier of this index.
   */
  SecurityIdentifier getSecurityIdentifier();

  /**
   * The latest intra-day key held in this index. Note that although the index may hold
   * many keys it does not mean that it hold the latest one which may have been updated in
   * the underlying data store but not yet retrieved.
   * 
   * @return the latest key in this index or {@code null} if none is indexed.
   */
  TemporalKey getLatestKey();

  /**
   * The latest intra-day key held in this index which refers to a record updated before a
   * specified instant. Note that although the index may hold many keys it does not mean
   * that it will hold a matching one for this predicate.
   * 
   * @param predicate the intra-day instant predicate.
   * @return the matching intra-day key in this index or {@code null} if none is indexed.
   */
  TemporalKey getLatestKey(Instant predicate);

  /**
   * The latest end-of-day key held in this index which refers to a record updated before
   * a specified {@link LocalDate}. Note that although the index may hold many keys it
   * does not mean that it will hold a matching one for this predicate.
   * 
   * @param predicate the end-of-day date predicate.
   * @return the matching end-of-day key in this index or {@code null} if none is indexed.
   */
  TemporalKey getEndOfDayKey(LocalDate predicate);

  /**
   * Add a new intra-day key to the index.
   * 
   * @param dataKey the key to add.
   * @param predicate the predicate which was used to find this key.
   */
  void addLatestKey(TemporalKey dataKey, Instant predicate);

  /**
   * Add a new end-of-day key to the index.
   * 
   * @param dataKey the key to add.
   * @param predicate the predicate which was used to find this key.
   */
  void addEndOfDayKey(TemporalKey dataKey, LocalDate predicate);

}
