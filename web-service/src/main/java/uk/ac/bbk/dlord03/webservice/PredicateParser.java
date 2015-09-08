package uk.ac.bbk.dlord03.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class PredicateParser {

  private static final Logger LOG =
        LoggerFactory.getLogger(PredicateParser.class);
  private final String predicate;
  private boolean isValid = false;
  private Type type = null;
  private Instant instantPredicate;
  private LocalDate localDatePredicate;

  private final DateTimeFormatter timeFormatter;
  private final DateTimeFormatter dateFormatter;

  public PredicateParser(String predicate) {
    super();
    this.predicate = predicate;
    timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
  }

  public boolean isValid() {
    try {
      parse();
    } catch (Exception e) {
      LOG.warn("Problem parsing command: {}", e.getMessage());
    }
    return isValid;
  }

  private void parse() {

    if (type != null) {
      return;
    }

    if (predicate == null) {
      type = Type.LATEST;
      isValid = true;
      return;
    };

    if (predicate.length() == 6) {

      TemporalAccessor ta = parsePredicate(timeFormatter, predicate);
      LocalTime lt = LocalTime.from(ta);

      ZonedDateTime asof = ZonedDateTime.now();
      asof = asof.withHour(lt.getHour());
      asof = asof.withMinute(lt.getMinute());
      asof = asof.withSecond(lt.getSecond());
      asof = asof.truncatedTo(ChronoUnit.SECONDS);

      instantPredicate = getInstant(asof);
      if (instantPredicate != null) {
        isValid = true;
        type = Type.INTRADAY;
      }
    }

    if (predicate.length() == 8) {
      TemporalAccessor ta = parsePredicate(dateFormatter, predicate);
      localDatePredicate = getLocalDate(ta);
      if (localDatePredicate != null) {
        isValid = true;
        type = Type.ENDOFDAY;
      }
    }

  }

  private static TemporalAccessor parsePredicate(DateTimeFormatter f,
        String p) {
    try {
      return f.parse(p);
    } catch (DateTimeParseException e) {
      LOG.warn("Invalid predicate {}", p);
      return null;
    }
  }

  private static Instant getInstant(TemporalAccessor ta) {
    try {
      return Instant.from(ta);
    } catch (DateTimeException e) {
      LOG.warn("Invalid time predicate", e);
      return null;
    }
  }

  private static LocalDate getLocalDate(TemporalAccessor ta) {
    try {
      return LocalDate.from(ta);
    } catch (DateTimeException e) {
      LOG.warn("Invalid date predicate", e);
      return null;
    }
  }

  public Instant getInstant() {
    return instantPredicate;
  }

  public LocalDate getLocalDate() {
    return localDatePredicate;
  }

  public Type getType() {
    parse();
    return type;
  }

  enum Type {
    LATEST, INTRADAY, ENDOFDAY
  }

}
