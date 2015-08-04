package dlord03.cache;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Properties;

import javax.cache.CacheManager;

import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.security.SecurityIdentifier;
import dlord03.plugin.api.event.InvalidationReport;

public interface QueryService {

  CacheManager getCacheManager();

  Properties getProperties();

  void start();

  void stop();

  SecurityData getLatestValue(CacheType type, SecurityIdentifier security);

  SecurityData getLatestValue(CacheType type, SecurityIdentifier security,
    Instant before);

  SecurityData getEndOfDayValue(CacheType type, SecurityIdentifier security,
    LocalDate date);

  void handleInvalidationReport(CacheType type, InvalidationReport report);

}
