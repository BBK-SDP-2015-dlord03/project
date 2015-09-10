package uk.ac.bbk.dlord03.plugin.api.data.security;

/**
 * Simple utility implementation bean of {@linkplain SecurityIdentifier}.
 * 
 * @author David Lord
 *
 */
public class SimpleSecurityIdentifier implements SecurityIdentifier {

  private static final long serialVersionUID = 8352895893354801227L;

  private final IdentifierScheme scheme;
  private final String symbol;

  public SimpleSecurityIdentifier(IdentifierScheme scheme, String symbol) {
    super();
    this.scheme = scheme;
    this.symbol = symbol;
  }

  @Override
  public IdentifierScheme getScheme() {
    return scheme;
  }

  @Override
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
    return String.format("SecurityIdentifier(scheme=%s,symbol=%s)", scheme, symbol);
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
    return (this.scheme.equals(other.getScheme()) && this.symbol.equals(other.getSymbol()));
  }

}
