package dlord03.cache.index;

import dlord03.cache.data.DataType;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public class IndexKeyImpl implements IndexKey {

  private static final long serialVersionUID = -6938651146376002358L;

  private final IndexType indexType;
  private final DataType dataType;
  private final SecurityIdentifier securityIdentifier;

  public IndexKeyImpl(IndexType indexType, DataType dataType,
      SecurityIdentifier securityIdentifier) {
    super();
    this.indexType = indexType;
    this.dataType = dataType;
    this.securityIdentifier = securityIdentifier;
  }

  @Override
  public IndexType getIndexType() {
    return indexType;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public SecurityIdentifier getsecurityIdentifier() {
    return securityIdentifier;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof IndexKeyImpl)) return false;
    final IndexKeyImpl other = (IndexKeyImpl) obj;
    return (this.indexType.equals(other.indexType) && this.dataType.equals(other.dataType)
        && this.securityIdentifier.equals(other.securityIdentifier));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + indexType.hashCode();
    result = 31 * result + dataType.hashCode();
    result = 31 * result + securityIdentifier.hashCode();
    return result;
  }

}
