package dlord03.cache;

import javax.cache.Cache;

import dlord03.cache.data.SimpleKey;
import dlord03.cache.data.TemporalKey;
import dlord03.cache.index.Index;
import dlord03.cache.index.IndexKey;
import dlord03.plugin.api.data.SecurityData;

/**
 * Interface that defines common operations for accessing caches.
 * 
 * @author David Lord
 *
 */
public interface CacheController {

  void open();

  void close();

  Cache<IndexKey, Index> getIndexCache();

  <T extends SecurityData> Cache<SimpleKey, T> getLatestCache();

  <T extends SecurityData> Cache<TemporalKey, T> getTimestampedCache();

  <T extends SecurityData> Cache<TemporalKey, T> getDatedCache();

}
