package dlord03.cache.data;

import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.VolatilitySurface;

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

  final private String name;
  final private Class<? extends SecurityData> valueClass;

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
