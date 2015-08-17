package dlord03.cache.service;

import java.util.Properties;

import javax.cache.CacheManager;

import dlord03.cache.QueryService;

public class QueryServiceFactory {

  public static QueryService createService(CacheManager cacheManager,
    Properties properties) {
    final QueryServiceImpl service = new QueryServiceImpl();
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    return service;
  }

}
