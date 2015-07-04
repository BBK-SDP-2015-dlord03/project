package dlord03.plugin.api.data;

public interface DividendSchedule extends Value, Iterable<Dividend> {
  
  @Override
  String getIsin();
  
  String getCurrency();

}
