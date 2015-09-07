package uk.ac.bbk.dlord03.webservice;

public class PredicateParser {

  private final String predicate;

  public PredicateParser(String predicate) {
    super();
    this.predicate = predicate;
  }

  public boolean isValid() {
    return false;

  }

  /**
   * Returns the type of this predicate.
   * 
   * @return 0 if this is a latest predicate, 1 if it is an intraday predicate
   *         and 2 if it is an end of day predicate.
   */
  public int getType() {
    if (predicate == null)
      return 0;
    if (predicate.length() == 6)
      return 1;
    if (predicate.length() == 8)
      return 2;
    return 0;

  }

}
