package uk.ac.bbk.dlord03.webservice.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "query-result")
public class JsonBeanResult {

  @JsonProperty
  Object result;

  // JSON needs empty constructor.
  public JsonBeanResult() {
    super();
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

}
