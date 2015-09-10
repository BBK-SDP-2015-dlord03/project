package uk.ac.bbk.dlord03.cache.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.OptionType;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.ZonedDateTime;
import java.util.Properties;

/**
 * Example of an simple plug-in which returns {@link OptionContract} objects.
 * 
 * @author David Lord
 *
 */
public class OptionContractPluginImpl extends AbstractPluginImp<OptionContract> {

  private static final Logger LOG = LoggerFactory.getLogger(OptionContractPluginImpl.class);

  private Option[] options;

  public OptionContractPluginImpl() {
    super();
    options = new Option[] {new Option("BT.L", "BT Group Plc", "2016-08-01", OptionType.PUT),
        new Option("VOD.L", "Vodafone Group Plc", "2018-08-01", OptionType.CALL),
        new Option("LGEN.L", "Legal & General Group Plc", "2020-08-01", OptionType.PUT),
        new Option("ULVR.L", "Unilever plc", "2017-08-01", OptionType.CALL)};
  }

  @Override
  public void doOpen(Properties properties) {

    for (Option option : options) {
      addSomeRecords(option);
    }

    LOG.debug("Option plugin opened");

  }

  @Override
  public void doClose() {
    LOG.debug("Option plugin closed");
  }

  private void addSomeRecords(Option option) {
    addSomeRecords(option.symbol, option.name, option.expiry, option.type);
  }

  private void addSomeRecords(String ric, String name, String expiry, OptionType type) {

    si = new SimpleSecurityIdentifier(IdentifierScheme.RIC, ric);
    OptionContractImpl option;

    // Add some representative end of day records.
    final ZonedDateTime monthAgo = getOneMonthAgo();
    for (int i = 0; i < 28; i++) {
      option = new OptionContractImpl(si, monthAgo.plusDays(i), type, expiry, 25.5D, name);
      endOfDayRecords.add(option);
    }

    // Add some representative intra-day records.
    final ZonedDateTime dayAgo = getOneDayAgo();
    for (int i = 0; i < 20; i++) {
      option = new OptionContractImpl(si, dayAgo.plusHours(i), type, expiry, 25.5D, name);
      intraDayRecords.add(option);
    }

  }

  @Override
  protected OptionContract createLatest(SecurityIdentifier security) {

    Option found = null;
    for (Option option : options) {
      if (option.symbol.equals(security.getSymbol())) {
        found = option;
      }
    }

    if (found == null) {
      return null;
    }

    return new OptionContractImpl(security, ZonedDateTime.now(), found.type, found.expiry, 25.5D,
          found.name);
  }

  class Option {

    final String symbol;
    final String name;
    final String expiry;
    final OptionType type;

    public Option(String symbol, String name, String expiry, OptionType type) {
      super();
      this.symbol = symbol;
      this.name = name;
      this.expiry = expiry;
      this.type = type;
    }
  }

}
