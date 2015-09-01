package uk.ac.bbk.dlord03.plugin.api.event;

import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

public interface InvalidationReport {

  SecurityIdentifier getInvalidatedSecurity();

}
