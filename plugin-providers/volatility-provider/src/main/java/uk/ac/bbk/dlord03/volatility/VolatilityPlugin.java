package uk.ac.bbk.dlord03.volatility;

import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.VolatilitySurface;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

public class VolatilityPlugin implements Plugin<VolatilitySurface> {

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
  public VolatilitySurface getLatestValue(SecurityIdentifier security) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VolatilitySurface getLatestValue(SecurityIdentifier security,
        Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VolatilitySurface getEndOfDayValue(SecurityIdentifier security,
        LocalDate date) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterator<SecurityIdentifier> getValuesUpdatedSince(Instant time) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void registerInvalidationHandler(InvalidationReportHandler handler) {
    // TODO Auto-generated method stub

  }

}
