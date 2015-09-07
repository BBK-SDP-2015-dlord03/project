package uk.ac.bbk.dlord03.webservice;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/")
public class CacheQueryService {

  @Context
  private ServletContext context;

  @GET
  @Path("option/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public OptionContractResult getOption(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    final QueryService queryService = getQueryService();

    final SecurityIdentifier id;
    id = new SimpleSecurityIdentifier(IdentifierScheme.OCC, symbol);

    OptionContract option = null;
    PredicateParser predicate = new PredicateParser(asof);

    try {
      switch (predicate.getType()) {
        case 0:

          break;

        default:
          break;
      }
      option = queryService.getLatestValue(DataType.OPTION, id);
    } catch (Exception e) {
      throw new NotFoundException(e);
    }

    if (option == null)
      throw new NotFoundException();

    OptionContractResult result = new OptionContractResult();
    result.setName(option.getContractName());
    result.setExpiry(option.getExpiryDate());
    result.setType(option.getOptionType().toString());
    result.setStrike(option.getStrikePrice());
    result.setSymbol(option.getSecurityIdentifier().getSymbol());
    result.setUpdatedAt(
          option.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    return result;

  }

  private QueryService getQueryService() {
    QueryService qs = null;
    qs = (QueryService) context.getAttribute("queryService");
    return qs;
  }

}
