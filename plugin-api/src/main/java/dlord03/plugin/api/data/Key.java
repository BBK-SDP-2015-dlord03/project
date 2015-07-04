package dlord03.plugin.api.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface Key extends Serializable {
  
  String getIsin();
  
  ZonedDateTime getUpdatedAt();

}
