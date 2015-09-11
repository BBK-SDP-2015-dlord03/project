package uk.ac.bbk.dlord03.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.webservice.beans.DividendsJsonBean;
import uk.ac.bbk.dlord03.webservice.beans.OptionJsonBean;

import javax.servlet.ServletContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class QueryRestServlet<T> {

  private final static Logger LOG = LoggerFactory.getLogger(QueryRestServlet.class);

  @Context
  private ServletContext context;
  @Context
  private UriInfo uriInfo;

  @GET
  @Path("option/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public OptionJsonBean getOption(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    validateRequest();
    MultivaluedMap<String, String> queryParams;
    queryParams = new MultivaluedHashMap<>(uriInfo.getQueryParameters());
    queryParams.add("symbolType", IdentifierScheme.OCC.toString());
    queryParams.add("symbol", symbol);
    queryParams.add("dataType", DataType.OPTION.toString());

    OptionContract option;
    option = executeQuery(queryParams);
    return new OptionJsonBean(option);

  }

  @GET
  @Path("dividend/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public DividendsJsonBean getDividend(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    validateRequest();
    MultivaluedMap<String, String> queryParams;
    queryParams = new MultivaluedHashMap<>(uriInfo.getQueryParameters());
    queryParams.add("symbolType", IdentifierScheme.RIC.toString());
    queryParams.add("symbol", symbol);
    queryParams.add("dataType", DataType.DIVIDEND.toString());

    DividendSchedule dividends;
    dividends = executeQuery(queryParams);
    return new DividendsJsonBean(dividends);

  }

  private <V extends SecurityData> V executeQuery(MultivaluedMap<String, String> params) {
    try {
      final CommandParser command = new CommandParser(params);
      final QueryCommand query = createCacheQuery(command);
      return query.execute();
    } catch (BadRequestException bre) {
      LOG.warn(bre.getMessage());
      throw bre;
    } catch (NotFoundException nfe) {
      throw nfe;
    } catch (Exception e) {
      LOG.error(e.getMessage());;
      throw e;
    }
  }

  private void validateRequest() {
    uriInfo.getQueryParameters().forEach((paramName, paramValue) -> {
      if (!"asof".equals(paramName) && !"symbol".equals(paramName)) {
        LOG.info("Invalid parameter {}={}", paramName, paramValue);
        throw new BadRequestException();
      }
    });
    LOG.info("Received request: {}", uriInfo.getRequestUri());
  }

  private QueryCommand createCacheQuery(CommandParser command) {
    return new QueryCommand(getQueryService(), command);
  }

  private QueryService getQueryService() {
    QueryService qs = null;
    qs = (QueryService) context.getAttribute("queryService");
    return qs;
  }

}
