package uk.ac.bbk.dlord03.cache.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialisationUtils {

  /**
   * Private constructor to prevent instantiation of utility class.
   */
  private SerialisationUtils() {}

  /**
   * Simulates a round trip through the cache.
   * 
   * @param object the object to store in the cache
   * @return the same object returned from the cache
   */
  @SuppressWarnings("unchecked")
  public static <T> T serializeRoundTrip(T object) {
    T roundTrippedObject = null;
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      final ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.close();
      baos.close();
      final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
      final ObjectInputStream ois = new ObjectInputStream(bais);
      roundTrippedObject = (T) ois.readObject();
      ois.close();
      bais.close();

    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return roundTrippedObject;

  }

}
