package dlord03.plugin.api.data.security;

import java.io.Serializable;

public interface SecurityIdentifier extends Serializable {

  public IdentifierScheme getScheme();

  public String getSymbol();

}
