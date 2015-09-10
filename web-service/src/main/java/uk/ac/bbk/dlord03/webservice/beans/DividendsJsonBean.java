package uk.ac.bbk.dlord03.webservice.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dividends")
public class DividendsJsonBean {

  @JsonProperty
  String symbol;
  @JsonProperty
  String currency;
  @JsonProperty
  String updatedAt;
  @JsonProperty
  List<DividendJsonBean> dividendBeans;

  // JSON needs empty constructor.
  public DividendsJsonBean() {
    super();
  }

  public DividendsJsonBean(DividendSchedule dividends) {
    dividendBeans = new ArrayList<>();
    this.symbol = dividends.getSecurityIdentifier().getSymbol();
    this.currency = dividends.getCurrency();
    ZonedDateTime timestamp = dividends.getUpdatedAt();
    this.updatedAt = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    for (Dividend dividend : dividends) {
      dividendBeans.add(new DividendJsonBean(dividend));
    }
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<DividendJsonBean> getDividendBeans() {
    return dividendBeans;
  }

  public void setDividendBeans(List<DividendJsonBean> dividendBeans) {
    this.dividendBeans = dividendBeans;
  }

}
