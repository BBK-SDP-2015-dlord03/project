package uk.ac.bbk.dlord03.webservice;

public class OptionContractResult {

  String name;
  String type;
  String expiry;
  String symbol;
  double strikePrice;
  String updatedAt;

  public OptionContractResult() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getExpiry() {
    return expiry;
  }

  public void setExpiry(String expiry) {
    this.expiry = expiry;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getStrikePrice() {
    return strikePrice;
  }

  public void setStrike(double strikePrice) {
    this.strikePrice = strikePrice;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String recordUpdatedAt) {
    this.updatedAt = recordUpdatedAt;
  }

}
