package uk.ac.bbk.dlord03.webservice;

import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dividends")
public class DividendsJsonBean {

  public DividendsJsonBean() {
    super();
  }

  public DividendsJsonBean(DividendSchedule dividends) {

  }

}
