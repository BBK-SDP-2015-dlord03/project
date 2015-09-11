package uk.ac.bbk.dlord03.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MultivaluedMap;

public class CommandParser {

  private static final Logger LOG = LoggerFactory.getLogger(CommandParser.class);
  private String predicate;
  private boolean isValid = false;
  private CommandType commandType = null;
  private DataType dataType;
  private SecurityIdentifier securityId;
  private Instant instantPredicate;
  private LocalDate localDatePredicate;

  private final DateTimeFormatter timeFormatter;
  private final DateTimeFormatter dateFormatter;

  public CommandParser() {
    super();
    timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
  }

  public CommandParser(MultivaluedMap<String, String> params) {

    this();

    String dataTypeParam = getRequiredParam(params, "dataType");
    dataType = DataType.valueOf(dataTypeParam);

    String symbolTypeParam = getRequiredParam(params, "symbolType");
    IdentifierScheme scheme = IdentifierScheme.valueOf(symbolTypeParam);

    String symbolParam = getRequiredParam(params, "symbol");
    securityId = new SimpleSecurityIdentifier(scheme, symbolParam);

    predicate = getParam(params, "asof").orElse(null);

  }

  public CommandParser(String predicate) {
    this();
    this.predicate = predicate;
  }

  static private String getRequiredParam(MultivaluedMap<String, String> params, String param) {
    return getParam(params, param)
          .orElseThrow(() -> new BadRequestException("Missing required parameter " + param));
  }

  static private Optional<String> getParam(MultivaluedMap<String, String> params, String param) {
    String paramValue = params.getFirst(param);
    if (paramValue == null) {
      return Optional.empty();
    }
    return Optional.of(paramValue);
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

    if (commandType != null) {
      return;
    }

    if (predicate == null) {
      commandType = CommandType.LATEST_QUERY;
      isValid = true;
      return;
    };

    if (predicate.length() == 6) {

      TemporalAccessor ta = parsePredicate(timeFormatter, predicate);
      instantPredicate = getInstant(ta);
      if (instantPredicate != null) {
        isValid = true;
        commandType = CommandType.INTRADAY_QUERY;
      }
    }

    if (predicate.length() == 8) {
      TemporalAccessor ta = parsePredicate(dateFormatter, predicate);
      localDatePredicate = getLocalDate(ta);
      if (localDatePredicate != null) {
        isValid = true;
        commandType = CommandType.ENDOFDAY_QUERY;
      }
    }

  }

  private static TemporalAccessor parsePredicate(DateTimeFormatter f, String p) {
    try {
      return f.parse(p);
    } catch (DateTimeParseException e) {
      LOG.warn("Can not interpret predicate {} as a valid date or time.", p);
      return null;
    }
  }

  private static Instant getInstant(TemporalAccessor ta) {
    try {
      LocalTime lt = LocalTime.from(ta);
      ZonedDateTime asof = ZonedDateTime.now();
      asof = asof.withHour(lt.getHour());
      asof = asof.withMinute(lt.getMinute());
      asof = asof.withSecond(lt.getSecond());
      asof = asof.truncatedTo(ChronoUnit.SECONDS);
      return Instant.from(asof);
    } catch (DateTimeException e) {
      LOG.warn("Invalid time predicate", e);
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  private static LocalDate getLocalDate(TemporalAccessor ta) {
    try {
      return LocalDate.from(ta);
    } catch (DateTimeException e) {
      LOG.warn("Invalid date predicate", e);
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public Instant getInstant() {
    return instantPredicate;
  }

  public LocalDate getLocalDate() {
    return localDatePredicate;
  }

  public CommandType getCommandType() {
    parse();
    return commandType;
  }

  public DataType getDataType() {
    return dataType;
  }

  public SecurityIdentifier getSecurityIdentifier() {
    return securityId;
  }

  enum CommandType {
    LATEST_QUERY, INTRADAY_QUERY, ENDOFDAY_QUERY
  }

}
