package uk.ac.bbk.dlord03.webservice;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

public class QueryCommand {

  final private QueryService service;
  final private CommandParser command;

  public QueryCommand(QueryService service, CommandParser command) {
    this.service = service;
    this.command = command;
  }

  public <T extends SecurityData> T execute() {

    if (!command.isValid()) {
      throw new BadRequestException();
    }

    SecurityIdentifier id = command.getSecurityIdentifier();
    DataType type = command.getDataType();
    T result = null;

    switch (command.getCommandType()) {
      case LATEST_QUERY:
        result = service.getLatestValue(type, id);
        break;
      case INTRADAY_QUERY:
        result = service.getLatestValue(type, id, command.getInstant());
        break;
      case ENDOFDAY_QUERY:
        result = service.getEndOfDayValue(type, id, command.getLocalDate());
        break;
      default:
        throw new BadRequestException("Invalid predicate.");
    }

    if (result == null) {
      throw new NotFoundException("Record not found.");
    }

    return result;
  }

}
