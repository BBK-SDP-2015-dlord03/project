package uk.ac.bbk.dlord03.plugin.api.data;

/**
 * A dividend schedule represents a predicted future stream of dividend payments
 * on a stock.
 * 
 * @author David Lord
 *
 */
public interface DividendSchedule extends SecurityData, Iterable<Dividend> {

  /**
   * The currency in which the dividends in this stream are paid.
   * 
   * @return the ISO 4217 currency code.
   */
  String getCurrency();

}
