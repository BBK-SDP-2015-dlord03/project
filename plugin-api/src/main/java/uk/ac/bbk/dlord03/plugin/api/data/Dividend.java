package uk.ac.bbk.dlord03.plugin.api.data;

import java.io.Serializable;
import java.time.LocalDate;

public interface Dividend extends Serializable {

  LocalDate getDate();

  Double getAmount();

}
