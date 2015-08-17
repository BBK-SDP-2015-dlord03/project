package dlord03.cache.service;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.event.InvalidationReport;

public interface PluginInvalidationReportHandler {
  
  void handleInvalidationReport(DataType type, InvalidationReport report);

}
