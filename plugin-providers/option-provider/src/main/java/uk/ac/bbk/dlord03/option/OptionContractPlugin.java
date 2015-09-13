package uk.ac.bbk.dlord03.option;

import uk.ac.bbk.dlord03.plugin.api.Plugin;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.OptionType;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

/**
 * 
 * A simple plug-in for option contracts. This plug-in is backed by tab separated text
 * file containing option contract details. On each request the text file is opened and
 * parsed for records matching the query criteria.
 * <p>
 * This implementation is deliberately unoptimised. It reads the text file on each
 * request. This is to simulate the heavy cost of retrieving data from an underlying data
 * source.
 * <p>
 * The option data in the test file has no versions. This is typical of some types of data
 * which don't change over time. In this case there will be only one record that satisfies
 * all temporal queries. This should be seen in the query interface by the plug-in only
 * getting called once for each option contract. After the first request all subsequent
 * queries can be serviced from the cache.
 * 
 * @author David Lord
 *
 */
public class OptionContractPlugin implements Plugin<OptionContract> {

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
  public OptionContract getLatestValue(SecurityIdentifier security) {
    OptionContract result = null;
    try (Scanner dataSource =
          new Scanner(this.getClass().getResourceAsStream("/data/optioncontracts.txt"))) {
      while (dataSource.hasNextLine()) {
        String record = dataSource.nextLine();
        try (Scanner recordScanner = new Scanner(record)) {
          recordScanner.useDelimiter("\t");
          if (recordScanner.hasNextDouble()) {
            Double strike = recordScanner.nextDouble();
            String symbol = recordScanner.next();
            String type = recordScanner.next();
            String expiry = recordScanner.next();
            String updated = recordScanner.next();
            if (symbol.equals(security.getSymbol())) {
              result = createOption(strike, symbol, type, expiry, updated);
              break;
            }
          }
        }
      }
    }
    return result;
  }

  public InvalidationReportHandler getHandler() {
    return handler;
  }

  private OptionContract createOption(Double strike, String symbol, String type, String expiry,
        String updated) {

    OptionType optionType = OptionType.valueOf(type);

    SecurityIdentifier id;
    id = new SimpleSecurityIdentifier(IdentifierScheme.OCC, symbol);

    TemporalAccessor parsedUpdated;
    parsedUpdated = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(updated);
    ZonedDateTime updatedAt = ZonedDateTime.from(parsedUpdated);

    return new OptionContractImpl(id, updatedAt, optionType, expiry, strike);
  }

  @Override
  public OptionContract getLatestValue(SecurityIdentifier security, Instant before) {
    // There is only one record for each option (the latest value).
    return getLatestValue(security);
  }

  @Override
  public OptionContract getEndOfDayValue(SecurityIdentifier security, LocalDate date) {
    // There is only one record for each option (the latest value).
    return getLatestValue(security);
  }

  @Override
  public Iterator<SecurityIdentifier> getValuesUpdatedSince(Instant time) {
    return null;
  }

  @Override
  public void registerInvalidationHandler(InvalidationReportHandler handler) {
    this.handler = handler;
  }

}
