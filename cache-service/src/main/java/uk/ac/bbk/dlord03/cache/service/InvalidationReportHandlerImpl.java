package uk.ac.bbk.dlord03.cache.service;

import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReport;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

public class InvalidationReportHandlerImpl<T extends SecurityData>
      implements InvalidationReportHandler {

  private final Class<T> dataType;
  private final PluginInvalidationReportHandler handler;

  public InvalidationReportHandlerImpl(PluginInvalidationReportHandler handler,
        Class<T> dataType) {

    super();

    if (dataType == null)
      throw new IllegalArgumentException("Invalid Data Type");

    if (handler == null)
      throw new IllegalArgumentException("Invalid Handler");

    this.dataType = dataType;

    this.handler = handler;
  }

  @Override
  public void invalidate(InvalidationReport report) {
    handler.handleInvalidationReport(dataType, report);
  }

}
