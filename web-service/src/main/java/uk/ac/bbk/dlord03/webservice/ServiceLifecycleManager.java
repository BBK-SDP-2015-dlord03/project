package uk.ac.bbk.dlord03.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.service.QueryServiceFactory;

import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceLifecycleManager implements ServletContextListener {

  private static final Logger LOG =
        LoggerFactory.getLogger(ServiceLifecycleManager.class);

  private QueryService queryService;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    LOG.info("Query service starting.");
    System.setProperty("hazelcast.logging.type", "slf4j");
    final CachingProvider cachingProvider = Caching.getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    final Properties props = getProperties();
    this.queryService = QueryServiceFactory.createService(cacheManager, props);
    this.queryService.start();
    LOG.info("Query service started.");
  }

  private Properties getProperties() {

    Properties p = new Properties();

    p.setProperty("option.plugin.classname",
          "uk.ac.bbk.dlord03.option.OptionContractPlugin");

    p.setProperty("dividend.plugin.classname",
          "uk.ac.bbk.dlord03.dividend.DividendPlugin");

    p.setProperty("volatility.plugin.classname",
          "uk.ac.bbk.dlord03.volatility.VolatilityPlugin");

    return p;
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    LOG.info("Query service stopping.");
    queryService.stop();
    LOG.info("Query service stopped.");
  }

}
