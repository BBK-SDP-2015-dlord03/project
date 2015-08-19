package dlord03.plugin.api.data.security;

import java.io.Serializable;

public class SecurityIdentifier implements Serializable {

  private static final long serialVersionUID = 8352895893354801227L;

  private final IdentifierScheme scheme;
  private final String symbol;

  public SecurityIdentifier(IdentifierScheme scheme, String symbol) {
    super();
    this.scheme = scheme;
    this.symbol = symbol;
  }

  public IdentifierScheme getScheme() {
    return scheme;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + scheme.hashCode();
    result = 31 * result + symbol.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("SecurityIdentifier(scheme=%s,symbol=%s)", scheme,
      symbol);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SecurityIdentifier)) {
      return false;
    }
    final SecurityIdentifier other = (SecurityIdentifier) obj;
    return (this.scheme.equals(other.scheme)
      && this.symbol.equals(other.symbol));
  }

}
