package uk.ac.bbk.dlord03.cache.service;

import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReport;

public interface PluginInvalidationReportHandler {

  <T extends SecurityData> void handleInvalidationReport(Class<T> type,
        InvalidationReport report);

}
