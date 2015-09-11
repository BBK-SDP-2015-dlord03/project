package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import java.time.temporal.TemporalAccessor;

/**
 * A utility factory class used for generating {@link TemporalKey} objects.
 * 
 * @author David Lord
 *
 */
public class TemporalKeyGenerator {

  // Private constructor for utility class.
  private TemporalKeyGenerator() {}

  /**
   * Factory method to generate a {@link TemporalKey} object from a {@link DataType} and
   * {@link SecurityIdentifier} {@link TemporalAccessor} timestamp .
   * 
   * @param dataType the data type for the key
   * @param security the identifier for the key
   * @param timestamp the timestamp for the key
   * @return the key
   */
  public static TemporalKey generate(DataType dataType, SecurityIdentifier security,
        TemporalAccessor timestamp) {

    return new TemporalKeyImpl(dataType, security, timestamp);

  }

  /**
   * Factory method to generate a {@link TemporalKey} object from a {@link DataType} and
   * {@link SecurityIdentifier}. The timestamp for this key will be generated from the
   * supplied {@code SecurityIdentifier}.
   * 
   * @param dataType the data type for the key
   * @param securityData the identifier for the key
   * @return the key
   */
  public static TemporalKey generate(DataType dataType, SecurityData securityData) {

    final SecurityIdentifier security = securityData.getSecurityIdentifier();
    final TemporalAccessor timestamp = securityData.getUpdatedAt();

    return generate(dataType, security, timestamp);

  }

}
