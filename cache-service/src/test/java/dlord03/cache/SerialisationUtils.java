package dlord03.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialisationUtils {

  /**
   * Simulates a round trip through the cache.
   * 
   * @param object the object to store in the cache
   * @return the same object returned from the cache
   */
  @SuppressWarnings("unchecked")
  public static <T> T roundTrip(T object) {
    Object roundTrippedObject = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.close();
      baos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      roundTrippedObject = ois.readObject();
      ois.close();
      bais.close();

    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return (T) roundTrippedObject;

  }

}
