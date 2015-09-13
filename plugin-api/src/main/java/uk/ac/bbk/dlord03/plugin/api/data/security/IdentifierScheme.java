package uk.ac.bbk.dlord03.plugin.api.data.security;

/**
 * Security identifier types are the various methods by which a security product
 * or issue is identified. They are each managed and distributed by different
 * organisations.
 * 
 * @author David Lord
 *
 */
public enum IdentifierScheme {

  // @formatter:off
  
  /**
   * International Securities Identification Number.
   */
  ISIN, 
  /**
   * Reuters Instrument Code.
   */
  RIC, 
  /**
   * Committee on Uniform Security Identification Procedures.
   */
  CUSIP, 
  /**
   *  Stock Exchange Daily Official List.
   */
  SEDOL, 
  /**
   * Bloomberg Global Identifier.
   */
  BBGID,
  /**
   * Options Clearing Corporation Symbol.
   */
  OCC

  // @formatter:on
}
