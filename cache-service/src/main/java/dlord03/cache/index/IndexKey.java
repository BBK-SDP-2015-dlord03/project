package dlord03.cache.index;

import dlord03.cache.data.SimpleKey;

/**
 * A key for storing {@link Index} objects.
 * 
 * @author David Lord
 *
 */
public interface IndexKey extends SimpleKey {

  /**
   * The {@link IndexType} of this key.
   * 
   * @return the index type.
   */
  IndexType getIndexType();

}
