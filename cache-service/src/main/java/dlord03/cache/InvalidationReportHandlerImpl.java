package dlord03.cache;

import dlord03.plugin.api.event.InvalidationReport;
import dlord03.plugin.api.event.InvalidationReportHandler;

public class InvalidationReportHandlerImpl implements InvalidationReportHandler {

  final private CacheType cacheType;
  final private QueryService queryService;

  public InvalidationReportHandlerImpl(QueryService cacheService, CacheType cacheType) {
    super();
    this.cacheType = cacheType;
    this.queryService = cacheService;
  }

  @Override
  public void invalidate(InvalidationReport report) {
    queryService.handleInvalidationReport(cacheType, report);
  }

}
