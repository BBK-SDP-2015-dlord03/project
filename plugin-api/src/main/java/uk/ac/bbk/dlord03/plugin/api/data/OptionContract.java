package uk.ac.bbk.dlord03.plugin.api.data;

import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * An option contract is a financial derivative that represents a contract sold
 * by one party (the option writer) to another party (the option holder). The
 * contract offers the buyer the right, but not the obligation, to buy (call) or
 * sell (put) a security or other financial asset at an agreed-upon price (the
 * strike price) during a certain period of time or on a specific date (expiry
 * date).
 * </p>
 * For an example of the simple representation of these contracts see Yahoo!
 * Finance's options listing for Vodafone Group PLC (
 * <a href="http://finance.yahoo.com/q/op?s=VOD+Options">VOD</a> ). For the
 * details of an individual option details see the page of the October 2015
 * 37.00 Call (
 * <a href="http://finance.yahoo.com/q?s=VOD151016C00037000">VOD151016C00037000
 * </a>).
 * 
 */
public interface OptionContract extends SecurityData {

  /**
   * Get the human readable name for this option contract. This will be in the
   * format <i>Code-Expiry-Strike-Type</i> so that a September 2015 call option
   * on a UBS single stock at a strike price of 20.00 will be "UBS Sep 2015
   * 20.000 Call".
   * 
   * @return the option contract's name.
   */
  String getContractName();

  /**
   * Get the option type of this contract.
   * 
   * @return the option type.
   */
  OptionType getOptionType();

  /**
   * The expiry date of the option contract in ISO 8601 format YYYY-MM-DD.
   * 
   * @return the expiry date.
   */
  String getExpiryDate();

  /**
   * The strike price of the option contract.
   * 
   * @return the strike price.
   */
  Double getStrikePrice();

  /**
   * The unique identifier of the option contract. This will normally be an
   * {@link IdentifierScheme#OCC} code.
   * 
   * @return the unique identifier.
   */
  SecurityIdentifier getTicker();

}
