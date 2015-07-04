package dlord03.plugin.api.event;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface InvalidationHandler {

  void invalidate(SecurityIdentifier security);

}
