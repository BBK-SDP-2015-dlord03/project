package dlord03.plugin.api.data.security;

import dlord03.plugin.api.data.SecurityData;

public interface SecurityTermSheet extends SecurityData {
  
  String getName();
  
  String getIntrumentType();
  
  SecurityIdentifier getUnderlyingSecurity();

}
