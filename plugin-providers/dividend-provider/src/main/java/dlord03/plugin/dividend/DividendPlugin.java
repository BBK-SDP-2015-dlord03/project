package dlord03.plugin.dividend;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Properties;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReportHandler;

public class DividendPlugin implements Plugin<DividendSchedule> {

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
  public DividendSchedule getLatestValue(SecurityIdentifier security) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DividendSchedule getLatestValue(SecurityIdentifier security,
    Instant before) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DividendSchedule getEndOfDayValue(SecurityIdentifier security,
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
