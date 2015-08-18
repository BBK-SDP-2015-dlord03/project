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
  protected final List<T> endOfDayRecords;
  protected final List<T> intraDayRecords;
  protected SecurityIdentifier si;
  protected long latestHitCount = 0;
  protected long latestPredicateHitCount = 0;
  protected long endOfDayHitCount = 0;

  public AbstractPluginImp() {
    super();
    endOfDayRecords = new ArrayList<>();
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
    latestHitCount++;
    return getRecord(intraDayRecords, security, ZonedDateTime.now().plusYears(1000));
  }

  @Override
  public T getLatestValue(SecurityIdentifier security, Instant before) {
    latestPredicateHitCount++;
    ZonedDateTime predicate = before.atZone(ZoneId.systemDefault());
    return getRecord(intraDayRecords, security, predicate);
  }

  @Override
  public T getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    endOfDayHitCount++;
    ZonedDateTime predicate = date.atStartOfDay(ZoneId.systemDefault());
    return getRecord(endOfDayRecords, security, predicate);
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
    return latestHitCount + latestPredicateHitCount + endOfDayHitCount;
  }

  public long getLatestHitCount() {
    return latestHitCount;
  }

  public long getlatestPredicateHitCount() {
    return latestPredicateHitCount;
  }

  public long getEndOfDayHitCount() {
    return endOfDayHitCount;
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

  protected T getRecord(List<T> list, SecurityIdentifier security, ZonedDateTime before) {

    T latest = null;
    for (T record : list) {
      if (record.getSecurityIdentifier().equals(security)
        && record.getUpdatedAt().isBefore(before)) {
        if (latest == null || record.getUpdatedAt().isAfter(latest.getUpdatedAt())) {
          latest = record;
        }
      }
    }

    return latest;

  }

  protected ZonedDateTime getOneDayAgo() {
    ZonedDateTime now = ZonedDateTime.now();
    return now.minusDays(1);

  }

  protected ZonedDateTime getOneMonthAgo() {
    ZonedDateTime now = ZonedDateTime.now();
    return now.minusMonths(1);
  }

}
