package uk.ac.bbk.dlord03.plugin.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ExceptionTest {

  @Test
  public void createPluginException() {
    PluginException pe = new PluginException();
    assertNotNull(pe);
  }

  @Test
  public void createRecordNotException() {
    PluginException rnf = new RecordNotFoundException();
    assertNotNull(rnf);
  }

}
