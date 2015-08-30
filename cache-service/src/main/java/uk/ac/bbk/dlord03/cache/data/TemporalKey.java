package uk.ac.bbk.dlord03.cache.data;

import java.time.Instant;

/**
 * A data key for caching values from the plug-ins based on their time-stamp.
 * 
 * @author David Lord
 *
 */
public interface TemporalKey extends SimpleKey {

  /**
   * The timestamp of the value referred to by this key.
   * 
   * @return the timestamp.
   */
  Instant getTimestamp();

}
