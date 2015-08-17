package dlord03.cache.index;

import dlord03.cache.data.SimpleDataKey;

public interface IndexKey extends SimpleDataKey {

  IndexType getIndexType();

}
