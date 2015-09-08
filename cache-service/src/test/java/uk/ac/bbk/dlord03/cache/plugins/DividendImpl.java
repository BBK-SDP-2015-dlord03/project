package uk.ac.bbk.dlord03.cache.plugins;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;

import java.time.LocalDate;

public class DividendImpl implements Dividend {

  private static final long serialVersionUID = -6710619176780133168L;

  private final LocalDate date;
  private final Double amount;
  private final Boolean actual;

  public DividendImpl(LocalDate date, Double amount, Boolean actual) {
    super();
    this.date = date;
    this.amount = amount;
    this.actual = actual;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public Double getAmount() {
    return amount;
  }

  @Override
  public Boolean isActual() {
    return actual;
  }

}
