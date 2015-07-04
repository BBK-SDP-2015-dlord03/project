package dlord03.plugin.api;

import java.io.Closeable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.data.Key;
import dlord03.plugin.api.data.Value;
import dlord03.plugin.api.event.InvalidationHandler;

public interface Plugin<K extends Key, V extends Value> extends Closeable {
  
  void open(Properties properties);
  
  @Override
  void close();
  
  boolean isClosed();
  
  V getLatestValue(K key);

  V getLatestValue(K key, Instant before);

  V getEndOfDayValue(K key, LocalDate date);
  
  Iterator<Key> getKeysUpdatedSince(Instant time);
  
  void registerInvalidationHandler(InvalidationHandler<K> handler);

}
