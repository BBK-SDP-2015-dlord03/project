package uk.ac.bbk.dlord03.cache;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.service.QueryServiceFactory;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.Instant;
import java.time.LocalDate;

/**
 * The primary interface used for querying the underlying data systems. Users of the
 * system typically retrieve a reference to this interface by calling
 * {@link QueryServiceFactory#createService}.
 * 
 * @author David Lord
 *
 */
public interface QueryService {

  /**
   * Start the service. Calls to other methods will fail before this method is called.
   */
  void start();

  /**
   * Stop the service and dispose of any managed caches.
   */
  void stop();

  /**
   * Return the latest intra-day value of record's data type.
   *
   * @param type the type of data required.
   * @param security the security for which the data is required.
   * @return the latest version of the data or {@code null} if none exists.
   */
  <T extends SecurityData> T getLatestValue(DataType type, SecurityIdentifier security);

  /**
   * Return the latest intra-day value of record's data type as it was at a particular
   * instant.
   *
   * @param type the type of data required.
   * @param security the security for which the data is required.
   * @param before the predicate before which the data must exist.
   * @return the latest version of the data or {@code null} if none exists.
   */
  <T extends SecurityData> T getLatestValue(DataType type, SecurityIdentifier security,
        Instant before);

  /**
   * Return the end-of-day value of record's data type as it was on a particular date.
   * 
   * @param type the type of data required.
   * @param security the security for which the data is required.
   * @param date the predicate date on which the data existed.
   * @return the version of the data for that day or {@code null} if none exists.
   */
  <T extends SecurityData> T getEndOfDayValue(DataType type, SecurityIdentifier security,
        LocalDate date);

}
