package dlord03.cache.data;

import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.VolatilitySurface;

/**
 * 
 * Represents the different types of data held in a cache and the classes that represent them.
 * 
 * @author david
 *
 */
public enum DataType {

  // @formatter:off
  OPTION("option", OptionContract.class), 
  DIVIDEND("dividend", DividendSchedule.class), 
  VOLATILITY("volatility", VolatilitySurface.class);
  // @formatter:on

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
