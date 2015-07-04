package dlord03.plugin.instrument;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.Instrument;
import dlord03.plugin.api.data.Key;
import dlord03.plugin.api.event.InvalidationHandler;

public class InstrumentPlugin implements Plugin<Key, Instrument> {

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
  public Instrument getLatestValue(Key key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Instrument getLatestValue(Key key, Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Instrument getEndOfDayValue(Key key, LocalDate date) {
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
