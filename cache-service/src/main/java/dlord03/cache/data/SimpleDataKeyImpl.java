package dlord03.cache.data;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleDataKeyImpl implements SimpleDataKey {

  private static final long serialVersionUID = -2427181751666407572L;

  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;

  public SimpleDataKeyImpl(DataType dataType, SecurityIdentifier securityIdentifier) {
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
    if (this == obj)
      return true;
    if (!(obj instanceof SimpleDataKeyImpl))
      return false;
    final SimpleDataKeyImpl other = (SimpleDataKeyImpl) obj;
    return (this.securityIdentifier.equals(other.securityIdentifier)
      && this.dataType.equals(other.dataType));
  }

}
