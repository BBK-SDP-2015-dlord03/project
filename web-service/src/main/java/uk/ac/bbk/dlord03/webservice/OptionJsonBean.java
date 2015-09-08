package uk.ac.bbk.dlord03.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A simple container for an {@link OptionContract} result which can be easily
 * converted to a JSON representation.
 * 
 * @author David Lord
 *
 */
@XmlRootElement(name = "option")
public class OptionJsonBean {

  @JsonProperty
  String name;
  @JsonProperty
  String type;
  @JsonProperty
  String expiry;
  @JsonProperty
  String symbol;
  @JsonProperty
  double strikePrice;
  @JsonProperty
  String updatedAt;

  // JSON needs empty constructor.
  public OptionJsonBean() {
    super();
  }

  public OptionJsonBean(OptionContract option) {
    this.name = option.getContractName();
    this.expiry = option.getExpiryDate();
    this.type = option.getOptionType().toString();
    this.strikePrice = option.getStrikePrice();
    this.symbol = option.getSecurityIdentifier().getSymbol();
    ZonedDateTime timestamp = option.getUpdatedAt();
    this.updatedAt = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getExpiry() {
    return expiry;
  }

  public String getSymbol() {
    return symbol;
  }

  public double getStrikePrice() {
    return strikePrice;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setExpiry(String expiry) {
    this.expiry = expiry;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setStrikePrice(double strikePrice) {
    this.strikePrice = strikePrice;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

}
