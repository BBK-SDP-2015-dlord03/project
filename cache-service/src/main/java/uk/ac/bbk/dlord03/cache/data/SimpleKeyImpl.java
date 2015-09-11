package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * A simple implementation of the {@link SimpleKey} interface with correctly implemented
 * {@code hashcode()} and {@code equals()} methods.
 * 
 * @author David Lord
 *
 */
public class SimpleKeyImpl implements SimpleKey {

  private static final long serialVersionUID = -2427181751666407572L;

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;

  /**
   * Create a {@link SimpleKeyImpl} object from a {@link DataType} and
   * {@link SecurityIdentifier}.
   * 
   * @param dataType the data type for the key.
   * @param securityIdentifier the identifier for the key.
   */
  public SimpleKeyImpl(DataType dataType, SecurityIdentifier securityIdentifier) {
    super();
    this.dataType = dataType;
    this.securityIdentifier = securityIdentifier;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + dataType.hashCode();
    result = 31 * result + securityIdentifier.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("SimpleCacheKey(dataType=%s,securityIdentifier=%s)", dataType,
          securityIdentifier);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SimpleKeyImpl)) {
      return false;
    }
    final SimpleKeyImpl other = (SimpleKeyImpl) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
          && this.dataType.equals(other.dataType));
  }

}
