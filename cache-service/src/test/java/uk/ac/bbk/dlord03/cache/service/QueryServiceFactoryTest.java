package uk.ac.bbk.dlord03.cache.service;

import org.junit.Test;

import uk.ac.bbk.dlord03.cache.service.QueryServiceFactory;

/**
 * @author David Lord
 *
 */
public class QueryServiceFactoryTest {

  @Test
  public void test() {
    QueryServiceFactory.createService(null, null);
  }

}
