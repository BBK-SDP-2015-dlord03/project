package dlord03.plugin.api.event;

public interface InvalidationHandler<K> {
  
  void invalidate(K key);

}
