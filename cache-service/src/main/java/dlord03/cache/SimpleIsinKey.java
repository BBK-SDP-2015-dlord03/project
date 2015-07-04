package dlord03.cache;

import java.time.ZonedDateTime;

import dlord03.plugin.api.data.security.SecurityIdentifier;

/**
 * @author David Lord
 *
 */
@SuppressWarnings("serial")
public class SimpleIsinKey implements Key {

  private final SecurityIdentifier securityIdentifier;
  private final ZonedDateTime updatedAt;
  private final int hashCode;

  public SimpleIsinKey(SecurityIdentifier securityIdentifier,
      ZonedDateTime updatedAt) {
    super();
    this.securityIdentifier = securityIdentifier;
    this.updatedAt = updatedAt;
    this.hashCode =
        31 * securityIdentifier.hashCode() + updatedAt.hashCode();
  }

  @Override
  public SecurityIdentifier getSecurityIdentifier() {
    return securityIdentifier;
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
    return (this.securityIdentifier.equals(other.securityIdentifier)
        && this.updatedAt.equals(other.updatedAt));
  }

}
