package dlord03.cache.index;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import dlord03.cache.data.DataType;
import dlord03.cache.data.DataKey;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Index extends Serializable {

  DataType getDataType();

  SecurityIdentifier getSecurityIdentifier();

  DataKey getLatestKey();

  DataKey getLatestKey(Instant before);

  DataKey getEndOfDayKey(LocalDate date);

  void addLatestKey(DataKey dataKey, Instant before);

  void addEndOfDayKey(DataKey dataKey, LocalDate date);

}
