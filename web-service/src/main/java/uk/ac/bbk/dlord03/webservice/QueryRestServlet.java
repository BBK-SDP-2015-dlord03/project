package uk.ac.bbk.dlord03.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.DividendSchedule;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;
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

    logRequest();
    OptionJsonBean result = null;

    try {
      result = lookupOption(symbol, asof);
    } catch (BadRequestException bre) {
      LOG.warn(bre.getMessage());
      throw bre;
    } catch (NotFoundException nfe) {
      throw nfe;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return result;

  }

  @GET
  @Path("dividend/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public DividendsJsonBean getDividend(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    logRequest();
    DividendsJsonBean result = null;

    try {
      result = lookupDividend(symbol, asof);
    } catch (BadRequestException bre) {
      LOG.warn(bre.getMessage());
      throw bre;
    } catch (NotFoundException nfe) {
      throw nfe;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return result;
  }

  private DividendsJsonBean lookupDividend(String symbol, String asof) {
    final SecurityIdentifier id;
    id = createSecurityIdentifier(IdentifierScheme.RIC, symbol);
    final QueryCommand query = createCacheQuery(DataType.DIVIDEND);
    DividendSchedule dividends;
    dividends = query.getValue(id, asof);
    return new DividendsJsonBean(dividends);
  }

  private OptionJsonBean lookupOption(String symbol, String asof) {
    final SecurityIdentifier id;
    id = createSecurityIdentifier(IdentifierScheme.OCC, symbol);
    final QueryCommand query = createCacheQuery(DataType.OPTION);
    OptionContract option;
    option = query.getValue(id, asof);
    return new OptionJsonBean(option);
  }

  private SecurityIdentifier createSecurityIdentifier(IdentifierScheme scheme, String symbol) {
    return new SimpleSecurityIdentifier(scheme, symbol);
  }

  private void logRequest() {
    LOG.info("Received request: {}", uriInfo.getRequestUri());
  }

  private QueryCommand createCacheQuery(DataType type) {
    return new QueryCommand(type, getQueryService());
  }

  private QueryService getQueryService() {
    QueryService qs = null;
    qs = (QueryService) context.getAttribute("queryService");
    return qs;
  }

}
