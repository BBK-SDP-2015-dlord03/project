package uk.ac.bbk.dlord03.webservice.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.bbk.dlord03.plugin.api.data.Dividend;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dividend")
public class DividendJsonBean {

  @JsonProperty
  String date;
  @JsonProperty
  String amount;

  // JSON needs empty constructor.
  public DividendJsonBean() {
    super();
  }

  public DividendJsonBean(Dividend dividend) {
    this.date = dividend.getDate().toString();
    this.amount = dividend.getAmount().toString();
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

}
