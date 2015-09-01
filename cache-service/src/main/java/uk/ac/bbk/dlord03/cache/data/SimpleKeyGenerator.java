package uk.ac.bbk.dlord03.cache.data;

import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

public class SimpleKeyGenerator {

  public static SimpleKey generate(DataType dataType,
        SecurityIdentifier securityIdentifier) {

    return new SimplKeyImpl(dataType, securityIdentifier);

  }

}
