package dlord03.plugin.api.data.security;

import java.io.Serializable;

public class SecurityIdentifier implements Serializable {

  private static final long serialVersionUID = 1L;

  private final IdentifierScheme scheme;
  private final String symbol;
  private final int hashCode;

  public SecurityIdentifier(IdentifierScheme scheme, String symbol) {
    super();
    this.scheme = scheme;
    this.symbol = symbol;
    this.hashCode = 31 * scheme.hashCode() + symbol.hashCode();
  }

  public IdentifierScheme getScheme() {
    return scheme;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return String.format("SecurityIdentifier(scheme=%s,symbol=%s)", scheme, symbol);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof SecurityIdentifier))
      return false;
    final SecurityIdentifier other = (SecurityIdentifier) obj;
    return (this.scheme.equals(other.scheme) && this.symbol.equals(other.symbol));
  }

}
