package uk.ac.bbk.dlord03.cache.index;

/**
 * The types of possible indexes.
 * 
 * @author David Lord
 *
 */
public enum IndexType {

  // @formatter:off

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
  
  // @formatter:on

}
