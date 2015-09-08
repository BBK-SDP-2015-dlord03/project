package uk.ac.bbk.dlord03.plugin.api.data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A dividend represents the future payment of a dividend.
 * 
 * @author David Lord
 *
 */
public interface Dividend extends Serializable {

  /**
   * The date on which the dividend will be paid.
   * 
   * @return the payment date.
   */
  LocalDate getDate();

  /**
   * The currency amount of the dividend.
   * 
   * @return the amount.
   */
  Double getAmount();

  /**
   * Is this dividend an actual (announced) dividend or a projected dividend.
   * 
   * @return <code>true</code> if this is an actual dividend otherwise
   *         <code>false</code>.
   */
  Boolean isActual();

}
