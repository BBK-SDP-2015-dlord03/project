package dlord03.cache;

import java.io.Serializable;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Key extends Serializable {
  
  CacheType getCacheType();

  SecurityIdentifier getSecurityIdentifier();
  
  String getTimestamp();

}