package dlord03.plugin.api.data;

public interface Instrument extends Value {
  
  String getName();
  
  String getIntrumentType();
  
  String getUnderlyingIsin();

}
