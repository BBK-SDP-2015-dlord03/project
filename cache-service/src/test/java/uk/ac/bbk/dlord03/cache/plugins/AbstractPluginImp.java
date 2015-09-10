package uk.ac.bbk.dlord03.cache.plugins;

import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReport;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public abstract class AbstractPluginImp<T extends SecurityData> implements Plugin<T> {

  protected boolean isOpen = false;
  protected InvalidationReportHandler handler;
  protected final List<T> endOfDayRecords;
  protected final List<T> intraDayRecords;
  protected SecurityIdentifier si;
  protected long latestHitCount = 0;
  protected long latestPredicateHitCount = 0;
  protected long endOfDayHitCount = 0;
  private T latestRecord = null;

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

    if (latestRecord == null) {
      latestRecord = createLatest(security);
    }

    return latestRecord;

  }

  @Override
  public T getLatestValue(SecurityIdentifier security, Instant before) {
    latestPredicateHitCount++;
    final ZonedDateTime predicate = before.atZone(ZoneId.systemDefault());
    return getRecord(intraDayRecords, security, predicate);
  }

  @Override
  public T getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    endOfDayHitCount++;
    final ZonedDateTime predicate = date.atStartOfDay(ZoneId.systemDefault());
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

    latestRecord = null;

    if (handler == null) {
      return;
    }
    final InvalidationReport report = new InvalidationReport() {

      @Override
      public SecurityIdentifier getInvalidatedSecurity() {
        return si;
      }

    };
    handler.invalidate(report);
  }

  protected abstract void doOpen(Properties properties);

  protected abstract void doClose();

  protected abstract T createLatest(SecurityIdentifier security);

  protected T getRecord(List<T> list, SecurityIdentifier security, ZonedDateTime before) {

    return list.stream().filter(p -> p.getSecurityIdentifier().equals(security))
          .sorted(new SecurityDataComparator()).findFirst().orElse(null);

  }

  protected ZonedDateTime getOneDayAgo() {
    final ZonedDateTime now = ZonedDateTime.now();
    return now.minusDays(1);

  }

  protected ZonedDateTime getOneMonthAgo() {
    final ZonedDateTime now = ZonedDateTime.now();
    return now.minusMonths(1);
  }

  private static class SecurityDataComparator implements Comparator<SecurityData> {

    @Override
    public int compare(SecurityData o1, SecurityData o2) {
      return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
    }

  }

}
