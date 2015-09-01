package uk.ac.bbk.dlord03.plugin.api.data;

import java.io.Serializable;

public interface VolatilityPoint extends Serializable {

  double getStrikePrice();

  double getTimeToMaturity();

  double getVolatility();

}
