package dlord03.plugin.api.data.security;

public class SecurityIdentifier {

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

  // TODO: hash and equals.

}
