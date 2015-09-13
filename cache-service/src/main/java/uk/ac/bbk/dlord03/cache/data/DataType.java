package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.VolatilitySurface;

/**
 * Represents the different types of data held in a cache and the classes that represent
 * them.
 * 
 * @author David Lord
 *
 */
public enum DataType {

  // @formatter:off

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

  // @formatter:on

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

  public static DataType valueOf(Class<? extends SecurityData> valueClass) {
    if (valueClass.equals(OptionContract.class)) {
      return OPTION;
    }
    if (valueClass.equals(DividendSchedule.class)) {
      return DIVIDEND;
    }
    if (valueClass.equals(VolatilitySurface.class)) {
      return VOLATILITY;
    }
    return null;

  }

}
