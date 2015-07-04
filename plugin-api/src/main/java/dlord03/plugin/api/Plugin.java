package dlord03.plugin.api;

import java.io.Closeable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationHandler;

public interface Plugin<V extends SecurityData> extends Closeable {

  void open(Properties properties);

  @Override
  void close();

  boolean isClosed();

  V getLatestValue(SecurityIdentifier security);

  V getLatestValue(SecurityIdentifier security, Instant before);

  V getEndOfDayValue(SecurityIdentifier security, LocalDate date);

  Iterator<String> getValuesUpdatedSince(Instant time);

  void registerInvalidationHandler(InvalidationHandler handler);

}
