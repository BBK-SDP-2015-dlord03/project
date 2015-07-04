package dlord03.plugin.api.event;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface InvalidationReportHandler {

  void invalidate(SecurityIdentifier security);

}
