package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.VolatilitySurface;

/**
 * 
 * Represents the different types of data held in a cache and the classes that represent them.
 * 
 * @author David Lord
 * @formatter:off
 *
 */
public enum DataType {

  /**
   * Option contract details.
   */
  OPTION("option", OptionContract.class), 
  /**
   * Dividend schedule.
   */
  DIVIDEND("dividend", DividendSchedule.class), 
  /**
   * Volatility surface.
   */
  VOLATILITY("volatility", VolatilitySurface.class);

  private final String name;
  private final Class<? extends SecurityData> valueClass;

  private DataType(String name, Class<? extends SecurityData> clazz) {
    this.name = name;
    this.valueClass = clazz;
  }

  public String getName() {
    return name;
  }

  public Class<? extends SecurityData> getValueClass() {
    return valueClass;
  }

}
