package uk.ac.bbk.dlord03.webservice;

import uk.ac.bbk.dlord03.cache.QueryService;
import uk.ac.bbk.dlord03.cache.data.DataType;
import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.data.security.SimpleSecurityIdentifier;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
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
  public OptionJsonBean getOption(@PathParam("symbol") String symbol,
        @QueryParam("asof") String asof) {

    final CacheQueryCommand query = createCacheQuery(DataType.OPTION);
    final SecurityIdentifier id;
    id = new SimpleSecurityIdentifier(IdentifierScheme.OCC, symbol);

    OptionContract option = null;
    option = query.getValue(id, asof);

    OptionJsonBean result = new OptionJsonBean(option);
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
