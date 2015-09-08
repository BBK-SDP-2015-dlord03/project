package uk.ac.bbk.dlord03.dividend;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Example of a simple {@link DividendSchedule} implementation.
 * 
 * @author David Lord
 *
 */
public class DividendScheduleImpl implements DividendSchedule {

  private static final long serialVersionUID = -5547989426463288930L;

  private final SecurityIdentifier id;
  private final String currency;
  private final ZonedDateTime updatedAt;
  private final List<Dividend> dividends;

  public DividendScheduleImpl(SecurityIdentifier id, String currency,
        ZonedDateTime updatedAt) {
    super();
    this.id = id;
    this.currency = currency;
    this.updatedAt = updatedAt;
    this.dividends = new ArrayList<>();
  }

  public void addDividend(Dividend dividend) {
    dividends.add(dividend);
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return id;
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

}
