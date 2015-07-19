package dlord03.cache;

import static org.mockito.Mockito.mock;

import javax.cache.CacheManager;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTests {

  @Test
  public void testService() {
    CacheManager mockCacheManager = mock(CacheManager.class);
    Service service = new Service(mockCacheManager);
    Assert.assertNotNull(service);
  }

}
