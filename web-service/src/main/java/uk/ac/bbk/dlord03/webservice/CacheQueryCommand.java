package uk.ac.bbk.dlord03.webservice;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

public class CacheQueryCommand {

  final private DataType type;
  final private QueryService service;

  public CacheQueryCommand(DataType type, QueryService service) {
    super();
    this.type = type;
    this.service = service;
  }

  public <T extends SecurityData> T getValue(SecurityIdentifier id,
        String asof) {

    PredicateParser predicate = new PredicateParser(asof);
    if (predicate == null || !predicate.isValid()) {
      throw new BadRequestException("Invalid predicate.");
    }

    T result = null;

    switch (predicate.getType()) {
      case LATEST:
        result = service.getLatestValue(type, id);
        break;
      case INTRADAY:
        result = service.getLatestValue(type, id, predicate.getInstant());
        break;
      case ENDOFDAY:
        result = service.getEndOfDayValue(type, id, predicate.getLocalDate());
        break;
      default:
        throw new BadRequestException("Invalid predicate.");
    }

    if (result == null) {
      throw new NotFoundException();
    }

    return result;

  }

}
