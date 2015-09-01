package uk.ac.bbk.dlord03.cache.plugins;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;

import java.time.LocalDate;

public class DividendImpl implements Dividend {

  private static final long serialVersionUID = -6710619176780133168L;

  private final LocalDate date;
  private final Double amount;

  public DividendImpl(LocalDate date, Double amount) {
    super();
    this.date = date;
    this.amount = amount;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public Double getAmount() {
    return amount;
  }

}
