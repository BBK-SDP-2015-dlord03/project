package uk.ac.bbk.dlord03.cache;

import javax.cache.Cache;

import dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.cache.data.SimpleKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.index.Index;
import uk.ac.bbk.dlord03.cache.index.IndexKey;

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
