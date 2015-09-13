package uk.ac.bbk.dlord03.plugin.api.data;

/**
 * The type of an option contract.
 * 
 * @author David Lord
 */
public enum OptionType {

  // @formatter:off

  /**
   * The option to sell the underlying asset.
   */
  PUT, 
  /**
   * The option to buy the underlying asset.
   */
  CALL
  
  // @formatter:on

}
