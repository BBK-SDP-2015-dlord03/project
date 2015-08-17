package dlord03.cache.index;

import dlord03.cache.data.SimpleKey;

public interface IndexKey extends SimpleKey {

  IndexType getIndexType();

}
