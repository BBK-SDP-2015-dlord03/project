package dlord03.plugin.dividend;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.Key;
import dlord03.plugin.api.event.InvalidationHandler;

public class DividendPlugin
    implements Plugin<Key, DividendSchedule> {

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
  public DividendSchedule getLatestValue(Key key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DividendSchedule getLatestValue(Key key, Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DividendSchedule getEndOfDayValue(Key key, LocalDate date) {
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
