package dlord03.cache.plugins;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;
import dlord03.plugin.api.event.InvalidationReportHandler;

public abstract class AbstractPluginImp<T extends SecurityData> implements Plugin<T> {

  protected boolean isOpen = false;
  protected InvalidationReportHandler handler;
  protected final List<T> fixingRecords;
  protected final List<T> intraDayRecords;
  protected SecurityIdentifier si;
  protected long hitCount = 0;

  public AbstractPluginImp() {
    super();
    fixingRecords = new ArrayList<>();
    intraDayRecords = new ArrayList<>();
  }

  @Override
  public void open(Properties properties) {
    doOpen(properties);
    isOpen = true;
  }

  @Override
  public void close() {
    doClose();
    isOpen = false;
  }

  @Override
  public boolean isClosed() {
    return !isOpen;
  }

  @Override
  public T getLatestValue(SecurityIdentifier security) {
    return getRecord(intraDayRecords, ZonedDateTime.now().plusYears(1000));
  }

  @Override
  public T getLatestValue(SecurityIdentifier security, Instant before) {
    return getRecord(intraDayRecords, before.atZone(ZoneId.systemDefault()));
  }

  @Override
  public T getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    return getRecord(intraDayRecords, date.atStartOfDay(ZoneId.systemDefault()));
  }

  @Override
  public Iterator<SecurityIdentifier> getValuesUpdatedSince(Instant time) {
    return null;
  }

  @Override
  public void registerInvalidationHandler(InvalidationReportHandler handler) {
    this.handler = handler;
  }

  public long getHitCount() {
    return hitCount;
  }

  public void invalidateLatest() {
    if (handler == null)
      return;
    InvalidationReport report = new InvalidationReport() {

      @Override
      public SecurityIdentifier getInvalidatedSecurity() {
        return si;
      }

    };
    handler.invalidate(report);
  }

  abstract protected void doOpen(Properties properties);

  abstract protected void doClose();

  protected T getRecord(List<T> list, ZonedDateTime before) {

    T latest = null;
    for (T option : list) {

      if (option.getUpdatedAt().isBefore(before)) {
        if (latest == null || option.getUpdatedAt().isAfter(latest.getUpdatedAt())) {
          latest = option;
        }
      }

    }

    return latest;

  }

}
