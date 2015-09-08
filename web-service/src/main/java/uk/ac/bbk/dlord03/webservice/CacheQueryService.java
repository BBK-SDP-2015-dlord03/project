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

@Path("/")
public class CacheQueryService {

  private final static Logger LOG =
        LoggerFactory.getLogger(CacheQueryService.class);

  @Context
  private ServletContext context;

  @GET
  @Path("option/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public OptionJsonBean getOption(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    OptionJsonBean result = null;
    try {
      final CacheQueryCommand query = createCacheQuery(DataType.OPTION);
      final SecurityIdentifier id;
      id = new SimpleSecurityIdentifier(IdentifierScheme.OCC, symbol);
      OptionContract option;
      option = query.getValue(id, asof);
      result = new OptionJsonBean(option);
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

    DividendsJsonBean result = null;

    try {
      final CacheQueryCommand query = createCacheQuery(DataType.DIVIDEND);
      final SecurityIdentifier id;
      id = new SimpleSecurityIdentifier(IdentifierScheme.RIC, symbol);
      DividendSchedule dividends;
      dividends = query.getValue(id, asof);
      result = new DividendsJsonBean(dividends);
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

  private CacheQueryCommand createCacheQuery(DataType type) {
    return new CacheQueryCommand(type, getQueryService());
  }

  private QueryService getQueryService() {
    QueryService qs = null;
    qs = (QueryService) context.getAttribute("queryService");
    return qs;
  }

}
