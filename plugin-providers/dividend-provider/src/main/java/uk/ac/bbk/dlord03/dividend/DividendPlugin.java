package uk.ac.bbk.dlord03.dividend;

import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.Dividend;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

/**
 * 
 * A simple plug-in for dividend schedules. This plug-in is backed by tab separated text
 * file containing dividend details. On each request the text file is opened and parsed
 * for records matching the query criteria.
 * </p>
 * This implementation is deliberately unoptimised. It reads the text file on each
 * request. This is to simulate the heavy cost of retrieving data from an underlying data
 * source.
 * </p>
 * The dividend data in the test file holds four versions of a dividend schedule for
 * GlaxoSmithKline <a href="https://uk.finance.yahoo.com/q?s=GSK.L">GSK.L</a>.
 * </p>
 * 
 * @author David Lord
 *
 */
public class DividendPlugin implements Plugin<DividendSchedule> {

  private boolean isOpen = false;
  private InvalidationReportHandler handler;

  @Override
  public void open(Properties properties) {
    isOpen = true;
  }

  @Override
  public void close() {
    isOpen = false;

  }

  @Override
  public boolean isClosed() {
    return !isOpen;
  }

  @Override
  public DividendSchedule getLatestValue(SecurityIdentifier security) {
    Optional<DividendSchedule> result =
          getDividends(security).stream().max(new Comparator<DividendSchedule>() {

            @Override
            public int compare(DividendSchedule o1, DividendSchedule o2) {
              return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
            }
          });
    return result.orElse(null);
  }

  @Override
  public DividendSchedule getLatestValue(SecurityIdentifier security, Instant before) {
    Optional<DividendSchedule> result =
          getDividends(security).stream().filter(p -> p.getUpdatedAt().toInstant().isBefore(before))
                .max(new Comparator<DividendSchedule>() {

                  @Override
                  public int compare(DividendSchedule o1, DividendSchedule o2) {
                    return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
                  }
                });
    return result.orElse(null);
  }

  @Override
  public DividendSchedule getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    Instant predicate = getInstantFromLocalDate(date);
    Optional<DividendSchedule> result = getDividends(security).stream()
          .filter(p -> p.getUpdatedAt().toInstant().isBefore(predicate))
          .max(new Comparator<DividendSchedule>() {

            @Override
            public int compare(DividendSchedule o1, DividendSchedule o2) {
              return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
            }
          });
    return result.orElse(null);
  }

  private Instant getInstantFromLocalDate(LocalDate date) {
    ZonedDateTime now = ZonedDateTime.now();
    now = now.truncatedTo(ChronoUnit.DAYS);
    now = now.withYear(date.getYear());
    now = now.withMonth(date.getMonthValue());
    now = now.withDayOfMonth(date.getDayOfMonth());
    return now.toInstant();
  }

  @Override
  public Iterator<SecurityIdentifier> getValuesUpdatedSince(Instant time) {
    return null;
  }

  @Override
  public void registerInvalidationHandler(InvalidationReportHandler handler) {
    this.handler = handler;
  }

  public InvalidationReportHandler getHandler() {
    return handler;
  }

  private List<DividendSchedule> getDividends(SecurityIdentifier security) {

    List<DividendSchedule> result = new ArrayList<>();

    if (!"GSK.L".equals(security.getSymbol()))
      return result;

    try (Scanner dataSource = new Scanner(this.getClass().getResourceAsStream("/data/gsk.txt"))) {

      String currency = dataSource.nextLine().trim();
      DividendScheduleImpl divs = null;
      while (dataSource.hasNextLine()) {
        String line = dataSource.nextLine();
        try (Scanner lineScanner = new Scanner(line)) {
          lineScanner.useDelimiter("\t");
          String date = lineScanner.next();
          if (date.length() > 10) {
            if (divs != null) {
              result.add(divs);
            }
            ZonedDateTime updatedAt = getUpdatedAt(date);
            divs = new DividendScheduleImpl(security, currency, updatedAt);
          } else {
            String amount = lineScanner.next();
            divs.addDividend(createDividend(date, amount));
          }

        }
      }
      if (divs != null) {
        result.add(divs);
      }

    }

    return result;

  }

  private static Dividend createDividend(String date, String amount) {
    final LocalDate localDate = getLocalDate(date);
    final Double doubleAmount = Double.valueOf(amount);
    return new DividendImpl(localDate, doubleAmount, false);
  }

  private static LocalDate getLocalDate(String date) {
    TemporalAccessor parsed;
    parsed = DateTimeFormatter.ISO_LOCAL_DATE.parse(date);
    return LocalDate.from(parsed);
  }

  private static ZonedDateTime getUpdatedAt(String updatedAt) {
    TemporalAccessor parsedUpdated;
    parsedUpdated = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(updatedAt);
    return ZonedDateTime.from(parsedUpdated);
  }

}
