package dlord03.cache;

import java.time.Instant;
import java.time.LocalDate;

import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface KeyIndex {

  CacheType getCacheType();

  SecurityIdentifier getSecurityIdentifier();

  Key getLatestKey();

  Key getLatestKey(Instant before);

  Key getEndOfDayKey(LocalDate date);

  void addLatestKey(Key key);

  void addLatestKey(Key key, Instant before);

  void addEndOfDayKey(Key key, LocalDate date);

}
