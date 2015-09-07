package uk.ac.bbk.dlord03.option;

import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.OptionType;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Example of an simple {@link OptionContract} implementation.
 * 
 * @author David Lord
 *
 */
public class OptionContractImpl implements OptionContract {

  private static final long serialVersionUID = -7179505812155258961L;
  private final SecurityIdentifier securityIdentifier;
  private final ZonedDateTime updatedAt;
  private final OptionType optionType;
  private final String expiryDate;
  private final Double strikePrice;

  public OptionContractImpl(SecurityIdentifier securityIdentifier,
        ZonedDateTime updatedAt, OptionType intrumentType, String expiryDate,
        Double strikePrice) {
    super();
    this.securityIdentifier = securityIdentifier;
    this.updatedAt = updatedAt;
    this.optionType = intrumentType;
    this.expiryDate = expiryDate;
    this.strikePrice = strikePrice;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String getContractName() {
    String symbol = securityIdentifier.getSymbol().substring(0, 3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
    TemporalAccessor date = DateTimeFormatter.ISO_LOCAL_DATE.parse(expiryDate);
    String expires = formatter.format(date);
    String type = optionType.toString();
    return String.format("%s %s %.2f %s", symbol, expires, strikePrice, type);
  }

  @Override
  public OptionType getOptionType() {
    return optionType;
  }

  @Override
  public String getExpiryDate() {
    return expiryDate;
  }

  @Override
  public Double getStrikePrice() {
    return strikePrice;
  }

  @Override
  public SecurityIdentifier getTicker() {
    return securityIdentifier;
  }

}
