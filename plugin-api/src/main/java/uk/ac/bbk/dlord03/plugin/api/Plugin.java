package uk.ac.bbk.dlord03.plugin.api;

import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

import java.io.Closeable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

/**
 * The standard interface which must be implemented by all plug-in providers which wish to
 * contribute data to the cache. Implementers of this interface must provide a no argument
 * constructor and initialise themselves from the <code>Properties</code> passed to them
 * via the {@link #open(Properties)} command.
 * <p/>
 * Plug-in implementors must provide results for the required query methods by querying
 * their underlying data stores.
 * <p/>
 * Cache consistency must be maintained via registering the passed in
 * {@link InvalidationReportHandler} and calling it with the affected records whenever the
 * underlying data store makes changes to its data which will invalid previously cached
 * results.
 * 
 * @param <V> The <code>SecurityData</code> type that this plug-in provides.
 *
 * @author David Lord
 */
public interface Plugin<V extends SecurityData> extends Closeable {

  /**
   * Complete any initialisation operations required prior to receiving requests from the
   * cache service. This method will be called by the cache container.
   * 
   * @param properties the properties required by the plug-in in order to initialise
   *        itself.
   */
  void open(Properties properties);

  @Override
  void close();

  boolean isClosed();

  /**
   * Return the latest value from the underlying data store for a specified security.
   * 
   * @param security the unique {@link SecurityIdentifier} identifying the requested
   *        record.
   * @return the latest value of the record corresponding to the requested security or
   *         {@code null} if none is available.
   */
  V getLatestValue(SecurityIdentifier security);

  /**
   * Return the value from the underlying data store for a specified security which was
   * most recently updated before a specific instant.
   * 
   * @param security the unique {@link SecurityIdentifier} identifying the requested
   *        record.
   * @param before the instantaneous point in time before which the returned record must
   *        be the most recent.
   * @return the value of the record corresponding to the requested security last updated
   *         before the specified instant or {@code null} if none is available.
   */
  V getLatestValue(SecurityIdentifier security, Instant before);

  /**
   * Return the value from the underlying data store for a specified security which was
   * the official closing <i>fixing</i> for a specific date.
   * 
   * @param security the unique {@link SecurityIdentifier} identifying the requested
   *        record.
   * @param date the date for which the end of day <i>fixing</i> record is required.
   * @return the end-of-day value of the record corresponding to the requested security on
   *         the specified date or {@code null} if none is available.
   */
  V getEndOfDayValue(SecurityIdentifier security, LocalDate date);

  /**
   * Return a list of unique {@link SecurityIdentifier}'s which have been modified in the
   * underlying data store since a specific instant. The returned list can be empty if no
   * records have been updated or {@code null} which will be treated as if an empty list
   * were returned.
   * 
   * @param time the time from which updated records should be returned.
   * @return the list of updated {@link SecurityIdentifier}'s.
   */
  Iterator<SecurityIdentifier> getValuesUpdatedSince(Instant time);

  /**
   * In order to notify the cache that previously fetched records have been updated and
   * that records held in the cache are not longer valid the plug-in must notify the cache
   * by issuing an invalidation report handler. The handler is supplied to the plug-in via
   * this method call.
   * 
   * @param handler the {@link InvalidationReportHandler} to use for sending
   */
  void registerInvalidationHandler(InvalidationReportHandler handler);

}
