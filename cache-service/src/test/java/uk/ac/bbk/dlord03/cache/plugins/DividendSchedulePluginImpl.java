package uk.ac.bbk.dlord03.cache.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.ZonedDateTime;
import java.util.Properties;

/**
 * Example of an simple plug-in which returns {@link DividendSchedule} objects.
 * 
 * @author David Lord
 *
 */
public class DividendSchedulePluginImpl
      extends AbstractPluginImp<DividendSchedule> {

  private static final Logger LOG =
        LoggerFactory.getLogger(DividendSchedulePluginImpl.class);

  @Override
  protected void doOpen(Properties properties) {

    final String ric = "BT.L";
    si = new SimpleSecurityIdentifier(IdentifierScheme.RIC, ric);

    DividendSchedule schedule;

    // Add some representative end of day records.
    final ZonedDateTime monthAgo = getOneMonthAgo();
    for (int i = 0; i < 28; i++) {
      schedule = new DividendScheduleImpl(si, monthAgo.plusDays(i),
            si.getSymbol(), monthAgo.toLocalDate(), 234.0d, 1.05d);
      endOfDayRecords.add(schedule);
    }

    // Add some representative intra-day records.
    final ZonedDateTime dayAgo = getOneDayAgo();
    for (int i = 0; i < 20; i++) {
      schedule = new DividendScheduleImpl(si, dayAgo.plusHours(i),
            si.getSymbol(), dayAgo.toLocalDate(), 150.0d, 1.05d);
      intraDayRecords.add(schedule);
    }

    LOG.debug("Dividend plugin opened");

  }

  @Override
  protected void doClose() {

    LOG.debug("Dividend plugin closed");

  }

  @Override
  protected DividendSchedule createLatest(SecurityIdentifier security) {
    ZonedDateTime shortWhileAgo = ZonedDateTime.now().minusMinutes(5);
    return new DividendScheduleImpl(si, shortWhileAgo, si.getSymbol(),
          getOneDayAgo().toLocalDate(), 150.0d, 1.05d);
  }

}
