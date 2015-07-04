package dlord03.plugin.api.impl;

import java.time.ZonedDateTime;

import dlord03.plugin.api.data.Key;

/**
 * @author David Lord
 *
 */
@SuppressWarnings("serial")
public class SimpleIsinKey implements Key {

  private final String isin;
  private final ZonedDateTime updatedAt;
  private final int hashCode;

  public SimpleIsinKey(String isin, ZonedDateTime updatedAt) {
    super();
    this.isin = isin;
    this.updatedAt = updatedAt;
    this.hashCode = 31 * isin.hashCode() + updatedAt.hashCode();
  }

  @Override
  public String getIsin() {
    return isin;
  }

  @Override
  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof SimpleIsinKey))
      return false;
    final SimpleIsinKey other = (SimpleIsinKey) obj;
    return (this.isin.equals(other.isin)
        && this.updatedAt.equals(other.updatedAt));
  }

}
