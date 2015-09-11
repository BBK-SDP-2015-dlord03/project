package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility factory class for creating implementations of the {@link Index} interface.
 * 
 * @author David Lord
 *
 */
public class IndexFactory {

  // Private constructor for utility class.
  private IndexFactory() {}

  /**
   * Create an {@link Index} for data of a particular type and security.
   * 
   * @param dataType the data type of the index.
   * @param security the security of the index.
   * @return the new Index.
   */
  public static Index create(DataType dataType, SecurityIdentifier security) {

    return new IndexImpl(dataType, security);

  }

  /**
   * Create a new {@link Index} from an existing one.
   * 
   * @param index the index to copy from.
   * @return the new index.
   */
  public static Index create(Index index) {

    IndexImpl from = null;
    if (index != null && index instanceof IndexImpl) {
      from = (IndexImpl) index;
    }

    if (from == null) {
      throw new IllegalArgumentException();
    }

    IndexImpl to;

    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      final ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(from);
      oos.close();
      baos.close();
      final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      final ObjectInputStream ois = new ObjectInputStream(bais);
      to = (IndexImpl) ois.readObject();
      ois.close();
      bais.close();

    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return to;

  }

}
