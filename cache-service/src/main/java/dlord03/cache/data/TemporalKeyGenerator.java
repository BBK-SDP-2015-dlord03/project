package dlord03.cache.data;

import java.time.temporal.TemporalAccessor;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public class TemporalKeyGenerator {

  public static TemporalKey generate(DataType dataType, SecurityIdentifier security,
    TemporalAccessor timestamp) {

    return new TemporalKeyImpl(dataType, security, timestamp);

  };

}
