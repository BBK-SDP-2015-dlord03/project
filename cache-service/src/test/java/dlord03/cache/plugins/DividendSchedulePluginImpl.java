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

    // Add some representative fixing records.
    ZonedDateTime start = ZonedDateTime.parse("2015-08-01T00:00:00.000Z");
    for (int i = 0; i < 20; i++) {
      schedule = new DividendScheduleImpl(si, start.plusHours(i), "GBP",
        start.toLocalDate(), 234.0d, 1.05d);
      fixingRecords.add(schedule);

    }

    // Add some representative intra-day records.
    start = ZonedDateTime.parse("2015-07-01T17:00:00.000Z");
    for (int i = 0; i < 20; i++) {
      schedule = new DividendScheduleImpl(si, start.plusDays(i), "GBP",
        start.toLocalDate(), 150.0d, 1.05d);
      intraDayRecords.add(schedule);
    }

  }

  @Override
  protected void doClose() {

  }

}
