package dlord03.cache.service;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.event.InvalidationReport;
import dlord03.plugin.api.event.InvalidationReportHandler;

public class InvalidationReportHandlerImpl implements InvalidationReportHandler {

  final private DataType dataType;
  final private PluginInvalidationReportHandler handler;

  public InvalidationReportHandlerImpl(PluginInvalidationReportHandler handler,
    DataType dataType) {
    super();
    this.dataType = dataType;
    this.handler = handler;
  }

  @Override
  public void invalidate(InvalidationReport report) {
    handler.handleInvalidationReport(dataType, report);
  }

}
