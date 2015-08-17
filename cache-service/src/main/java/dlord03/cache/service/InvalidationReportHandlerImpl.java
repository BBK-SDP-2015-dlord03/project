package dlord03.cache.service;

import dlord03.cache.QueryService;
import dlord03.cache.data.DataType;
import dlord03.plugin.api.event.InvalidationReport;
import dlord03.plugin.api.event.InvalidationReportHandler;

public class InvalidationReportHandlerImpl implements InvalidationReportHandler {

  final private DataType dataType;
  final private QueryService queryService;

  public InvalidationReportHandlerImpl(QueryService cacheService, DataType dataType) {
    super();
    this.dataType = dataType;
    this.queryService = cacheService;
  }

  @Override
  public void invalidate(InvalidationReport report) {
    queryService.handleInvalidationReport(dataType, report);
  }

}
