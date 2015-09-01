package uk.ac.bbk.dlord03.cache.index;

import static uk.ac.bbk.dlord03.cache.support.SerialisationUtils.serializeRoundTrip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.cache.data.TemporalKey;
import uk.ac.bbk.dlord03.cache.data.TemporalKeyImpl;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

public class DateIndexEntryTest {

  TemporalAccessor predicate;
  TemporalKey key;
  SecurityIdentifier security;
  IndexEntry<? extends TemporalAccessor> indexEntry;

  @Before
  public void setUp() {
    predicate = LocalDate.now();
    security = new SimpleSecurityIdentifier(IdentifierScheme.RIC, "BT.L");
    key = new TemporalKeyImpl(DataType.DIVIDEND, security, predicate);
    indexEntry = new IndexEntry<>(key, predicate);
  }

  @Test
  public void testHashCode() {
    IndexEntry<? extends TemporalAccessor> newIndexEntry =
          serializeRoundTrip(indexEntry);
    Assert.assertEquals(indexEntry.hashCode(), newIndexEntry.hashCode());
  }

  @Test
  public void testGetKey() {
    Assert.assertEquals(key, indexEntry.getKey());
  }

  @Test
  public void testGetPredicate() {
    Assert.assertEquals(predicate, indexEntry.getPredicate());
  }

  @Test
  public void testCompareTo() {
    IndexEntry<? extends TemporalAccessor> newIndexEntry =
          serializeRoundTrip(indexEntry);
    Assert.assertEquals(0,
          indexEntry.compareTo((IndexEntry<TemporalAccessor>) newIndexEntry));
  }

  @Test
  public void testToString() {
    Assert.assertNotNull(indexEntry.toString());
  }

  @Test
  public void testEqualsObject() {
    IndexEntry<? extends TemporalAccessor> newIndexEntry =
          serializeRoundTrip(indexEntry);
    Assert.assertEquals(indexEntry, newIndexEntry);
  }

}
