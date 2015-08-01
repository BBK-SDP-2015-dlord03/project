package dlord03.cache;

import java.time.temporal.TemporalAccessor;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface KeyIndex {
  
  CacheType getCacheType();
  
  SecurityIdentifier getSecurityIdentifier();

  void add(Key key, TemporalAccessor asof);
  
  Key getLatestKey();
  
  Key getLatestKeyAsOf(TemporalAccessor asof);

}
