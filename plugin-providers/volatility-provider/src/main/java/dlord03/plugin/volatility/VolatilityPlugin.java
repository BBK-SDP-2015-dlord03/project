package dlord03.plugin.volatility;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.Key;
import dlord03.plugin.api.data.VolatilitySurface;
import dlord03.plugin.api.event.InvalidationHandler;

public class VolatilityPlugin
    implements Plugin<Key, VolatilitySurface> {

  @Override
  public void open(Properties properties) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isClosed() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public VolatilitySurface getLatestValue(Key key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VolatilitySurface getLatestValue(Key key, Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VolatilitySurface getEndOfDayValue(Key key, LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterator<Key> getKeysUpdatedSince(Instant time) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void registerInvalidationHandler(
      InvalidationHandler<Key> handler) {
    // TODO Auto-generated method stub
    
  }

}
