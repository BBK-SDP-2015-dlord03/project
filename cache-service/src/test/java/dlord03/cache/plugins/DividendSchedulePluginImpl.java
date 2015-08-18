package dlord03.cache.plugins;

import java.time.ZonedDateTime;
import java.util.Properties;

import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.security.IdentifierScheme;
import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * Example of an simple plug-in which returns {@link DividendSchedule} objects.
 * 
 * @author David Lord
 *
 */
public class DividendSchedulePluginImpl extends AbstractPluginImp<DividendSchedule> {

  @Override
  protected void doOpen(Properties properties) {

    String ric = "BT.L";
    si = new SecurityIdentifier(IdentifierScheme.RIC, ric);

    DividendSchedule schedule;

    // Add some representative end of day records.
    ZonedDateTime monthAgo = getOneMonthAgo();
    for (int i = 0; i < 28; i++) {
      schedule = new DividendScheduleImpl(si, monthAgo.plusDays(i), "GBP",
        monthAgo.toLocalDate(), 234.0d, 1.05d);
      endOfDayRecords.add(schedule);
    }

    // Add some representative intra-day records.
    ZonedDateTime dayAgo = getOneDayAgo();
    for (int i = 0; i < 20; i++) {
      schedule = new DividendScheduleImpl(si, dayAgo.plusHours(i), "GBP",
        dayAgo.toLocalDate(), 150.0d, 1.05d);
      intraDayRecords.add(schedule);
    }

  }

  @Override
  protected void doClose() {

  }

}
