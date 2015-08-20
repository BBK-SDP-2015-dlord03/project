package dlord03.cache.plugins;

import java.time.ZonedDateTime;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

/**
 * Example of an simple plug-in which returns {@link OptionContract} objects.
 * 
 * @author David Lord
 *
 */
public class OptionContractPluginImpl
  extends AbstractPluginImp<OptionContract> {

  private final static Logger LOG =
    LoggerFactory.getLogger(OptionContractPluginImpl.class);

  public OptionContractPluginImpl() {
    super();
  }

  @Override
  public void doOpen(Properties properties) {

    addSomeRecords("BT.L", "BT Group Plc", "2016-08-01");
    addSomeRecords("VOD.L", "Vodafone Group Plc", "2018-08-01");
    addSomeRecords("LGEN.L", "Legal & General Group Plc", "2020-08-01");
    addSomeRecords("ULVR.L", "Unilever plc", "2017-08-01");

    LOG.debug("Option plugin opened");

  }

  @Override
  public void doClose() {
    LOG.debug("Option plugin closed");
  }

  private void addSomeRecords(String ric, String name, String expiry) {

    si = new SimpleSecurityIdentifier(IdentifierScheme.RIC, ric);
    OptionContractImpl option;

    // Add some representative end of day records.
    final ZonedDateTime monthAgo = getOneMonthAgo();
    for (int i = 0; i < 28; i++) {
      option = new OptionContractImpl(si, monthAgo.plusDays(i), "PUT", expiry,
        25.5D, name);
      endOfDayRecords.add(option);
    }

    // Add some representative intra-day records.
    final ZonedDateTime dayAgo = getOneDayAgo();
    for (int i = 0; i < 20; i++) {
      option = new OptionContractImpl(si, dayAgo.plusHours(i), "PUT", expiry,
        25.5D, name);
      intraDayRecords.add(option);
    }

  }

}
