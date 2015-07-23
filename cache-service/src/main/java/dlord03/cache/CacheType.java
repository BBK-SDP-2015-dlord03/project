package dlord03.cache;

import dlord03.plugin.api.data.DividendSchedule;
import dlord03.plugin.api.data.OptionContract;
import dlord03.plugin.api.data.SecurityData;
import dlord03.plugin.api.data.VolatilitySurface;

public enum CacheType {
  
  OPTION ("option", OptionContract.class),
  DIVIDEND ("dividend", DividendSchedule.class),
  VOLATILITY ("dividend", VolatilitySurface.class);
  
  final private String name;
  final private Class<? extends SecurityData> valueClass;
  
  private CacheType(String name, Class<? extends SecurityData> clazz) {
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