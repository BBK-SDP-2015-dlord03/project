package dlord03.plugin.api.data;

import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * Represents a traded option contract. For an example of the simple representation of these
 * contracts see Yahoo! Finance's <a href="http://finance.yahoo.com/options">options</a> web page or
 * for a detailed view for one particlar ticker see
 * <a href="https://uk.finance.yahoo.com/q/op?s=VOD">Vodafone</a>.
 * 
 */
public interface OptionContract extends SecurityData {

  String getName();

  String getIntrumentType();

  String getExpiryDate();

  Double getStrikePrice();

  SecurityIdentifier getTicker();

}
