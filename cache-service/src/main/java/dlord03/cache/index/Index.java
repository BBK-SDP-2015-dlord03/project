package dlord03.cache.index;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import dlord03.cache.data.DataType;
import dlord03.cache.data.TemporalKey;
import dlord03.plugin.api.data.security.SecurityIdentifier;

public interface Index extends Serializable {

  DataType getDataType();

  SecurityIdentifier getSecurityIdentifier();

  TemporalKey getLatestKey();

  TemporalKey getLatestKey(Instant before);

  TemporalKey getEndOfDayKey(LocalDate date);

  void addLatestKey(TemporalKey dataKey, Instant before);

  void addEndOfDayKey(TemporalKey dataKey, LocalDate date);

}
