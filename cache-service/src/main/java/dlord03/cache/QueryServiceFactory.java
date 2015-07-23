package dlord03.cache;

import java.util.Properties;

import javax.cache.CacheManager;

public class QueryServiceFactory {
  
  public static QueryService createService(CacheManager cacheManager, Properties properties){
    QueryServiceImpl service = new QueryServiceImpl();
    service.setCacheManager(cacheManager);
    service.setProperties(properties);
    return service;
  }

}
