package dlord03.cache.service;

import java.util.Properties;

import javax.cache.CacheManager;

import dlord03.cache.QueryService;

public class QueryServiceFactory {

  /**
   * Create an instance of the {@link QueryService}.
   * 
   * @param cacheManager the JSR 107 cache manager to use.
   * @param properties the service specific properties required to create the
   *        service.
   * @return a new instance of the query service.
   */
  public static QueryService createService(CacheManager cacheManager,
    Properties properties) {
    final QueryServiceImpl service = new QueryServiceImpl();
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    return service;
  }

}
