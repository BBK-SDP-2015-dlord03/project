package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * A utility factory class used for generating {@link SimpleKey} objects.
 * 
 * @author David Lord
 *
 */
public class SimpleKeyGenerator {

  // Private constructor for utility class.
  private SimpleKeyGenerator() {}

  /**
   * Factory method to generate a {@link SimpleKey} object from a {@link DataType} and
   * {@link SecurityIdentifier}.
   * 
   * @param dataType the data type for the key.
   * @param securityIdentifier the identifier for the key.
   * @return the key.
   */
  public static SimpleKey generate(DataType dataType, SecurityIdentifier securityIdentifier) {

    return new SimpleKeyImpl(dataType, securityIdentifier);

  }

}
