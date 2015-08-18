package dlord03.cache.plugins;

import java.time.ZonedDateTime;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * Example of an simple plug-in which returns {@link OptionContract} objects.
 * 
 * @author David Lord
 *
 */
public class OptionContractPluginImpl extends AbstractPluginImp<OptionContract> {

  private final static Logger LOG =
    LoggerFactory.getLogger(OptionContractPluginImpl.class);

  public OptionContractPluginImpl() {
    super();
  }

  @Override
  public void doOpen(Properties properties) {

    String ric = "BT.L";
    String name = "British Telecom";
    String expiry = "2016-08-01";
    si = new SecurityIdentifier(IdentifierScheme.RIC, ric);
    OptionContractImpl option;

    // Add some representative end of day records.
    ZonedDateTime monthAgo = getOneMonthAgo();
    for (int i = 0; i < 28; i++) {
      option =
        new OptionContractImpl(si, monthAgo.plusDays(i), "PUT", expiry, 25.5D, name);
      endOfDayRecords.add(option);
    }

    // Add some representative intra-day records.
    ZonedDateTime dayAgo = getOneDayAgo();
    for (int i = 0; i < 20; i++) {
      option =
        new OptionContractImpl(si, dayAgo.plusHours(i), "PUT", expiry, 25.5D, name);
      intraDayRecords.add(option);
    }

    LOG.debug("Optino plugin opened");

  }

  @Override
  public void doClose() {
    LOG.debug("Option plugin closed");
  }

}
