package dlord03.cache.index;

/**
 * The types of possible indexes.
 * 
 * @author David Lord
 * @formatter:off
 *
 */
public enum IndexType {
  /**
   * An index of only the latest values.
   */
  LATEST,
  /**
   *  An index of only intra-day values.
   */
  INTRADAY,
  /**
   * An index of end-of-day values.
   */
  ENDOFDAY
}
