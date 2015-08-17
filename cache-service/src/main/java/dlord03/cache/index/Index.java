package dlord03.cache.index;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import dlord03.cache.data.DataType;
import dlord03.cache.data.TemporalDataKey;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Index extends Serializable {

  DataType getDataType();

  SecurityIdentifier getSecurityIdentifier();

  TemporalDataKey getLatestKey();

  TemporalDataKey getLatestKey(Instant before);

  TemporalDataKey getEndOfDayKey(LocalDate date);

  void addLatestKey(TemporalDataKey dataKey, Instant before);

  void addEndOfDayKey(TemporalDataKey dataKey, LocalDate date);

}
