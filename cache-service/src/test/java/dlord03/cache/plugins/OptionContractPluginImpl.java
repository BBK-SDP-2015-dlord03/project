package dlord03.cache.plugins;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.plugin.api.Plugin;
import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;
import dlord03.plugin.api.event.InvalidationReportHandler;

public class OptionContractPluginImpl implements Plugin<OptionContract> {

  private boolean isOpen = false;
  private InvalidationReportHandler handler;
  private final List<OptionContract> fixingRecords;
  private final List<OptionContract> intraDayRecords;
  private SecurityIdentifier si;
  private long hitCount = 0;

  private final static Logger LOG =
    LoggerFactory.getLogger(OptionContractPluginImpl.class);

  public OptionContractPluginImpl() {
    super();
    fixingRecords = new ArrayList<>();
    intraDayRecords = new ArrayList<>();
  }

  @Override
  public void open(Properties properties) {

    String ric = "BT.L";
    String name = "British Telecom";
    String expiry = "2016-08-01";
    si = new SecurityIdentifier(IdentifierScheme.RIC, ric);
    OptionContractImpl option;

    // Add some representative fixing records.
    ZonedDateTime start = ZonedDateTime.parse("2015-08-01T00:00:00.000Z");
    for (int i = 0; i < 20; i++) {
      option = new OptionContractImpl(si, start.plusHours(i), "PUT", expiry, 25.5D, name);
      fixingRecords.add(option);
    }

    // Add some representative intra-day records.
    start = ZonedDateTime.parse("2015-07-01T17:00:00.000Z");
    for (int i = 0; i < 20; i++) {
      option = new OptionContractImpl(si, start.plusDays(i), "PUT", expiry, 25.5D, name);
      intraDayRecords.add(option);
    }

    isOpen = true;
    LOG.debug("Opened");

  }

  @Override
  public void close() {
    isOpen = false;
    LOG.debug("Closed");
  }

  @Override
  public boolean isClosed() {
    return !isOpen;
  }

  @Override
  public OptionContract getLatestValue(SecurityIdentifier security) {
    return getOptionContract(intraDayRecords, ZonedDateTime.now().plusYears(1000));
  }

  @Override
  public OptionContract getLatestValue(SecurityIdentifier security, Instant before) {
    return getOptionContract(intraDayRecords, before.atZone(ZoneId.systemDefault()));
  }

  @Override
  public OptionContract getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    return getOptionContract(intraDayRecords, date.atStartOfDay(ZoneId.systemDefault()));
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

  private static OptionContract getOptionContract(List<OptionContract> list,
    ZonedDateTime before) {

    OptionContract latest = null;
    for (OptionContract option : list) {

      if (option.getUpdatedAt().isBefore(before)) {
        if (latest == null || option.getUpdatedAt().isAfter(latest.getUpdatedAt())) {
          latest = option;
        }
      }

    }

    return latest;

  }

}
