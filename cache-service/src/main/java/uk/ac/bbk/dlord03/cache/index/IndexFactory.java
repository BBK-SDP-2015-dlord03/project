package uk.ac.bbk.dlord03.cache.index;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IndexFactory {

  public static Index create(DataType dataType, SecurityIdentifier securityIdentifier) {

    return new IndexImpl(dataType, securityIdentifier);

  }

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
