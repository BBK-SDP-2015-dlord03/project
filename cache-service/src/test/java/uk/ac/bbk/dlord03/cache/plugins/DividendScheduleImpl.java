package uk.ac.bbk.dlord03.cache.plugins;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DividendScheduleImpl implements DividendSchedule {

  private static final long serialVersionUID = 2199500491754781854L;

  private final SecurityIdentifier si;
  private final ZonedDateTime updatedAt;
  private final String currency;
  private final List<Dividend> dividends;

  public DividendScheduleImpl(SecurityIdentifier si, ZonedDateTime updatedAt,
        String currency, LocalDate firstDividendDate,
        Double firstDividendAmount, Double growthRate) {
    super();
    this.si = si;
    this.updatedAt = updatedAt;
    this.currency = currency;
    this.dividends = new ArrayList<>();

    // Generate twenty four months' worth of sample dividends
    double growth = 1.00d;
    LocalDate date;
    for (int i = 0; i < 23; i++) {
      date = firstDividendDate.plusMonths(i);
      dividends
            .add(new DividendImpl(date, firstDividendAmount * growth, false));
      growth = +growth * growthRate;
    }

  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return si;
  }

  @Override
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public Iterator<Dividend> iterator() {
    return dividends.iterator();
  }

  @Override
  public String getCurrency() {
    return currency;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DividendScheduleImpl)) {
      return false;
    }
    DividendScheduleImpl other = (DividendScheduleImpl) obj;
    if (!this.si.equals(other.si))
      return false;
    if (!this.currency.equals(other.currency))
      return false;
    if (!this.updatedAt.equals(other.updatedAt))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format(
          "DividendScheduleImpl[Security=%s,Currency=%s,UpdatedAt=%s]", si,
          currency, updatedAt);
  }

}
