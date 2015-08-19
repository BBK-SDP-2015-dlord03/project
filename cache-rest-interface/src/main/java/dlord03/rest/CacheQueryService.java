package dlord03.rest;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dlord03.cache.QueryService;
import dlord03.cache.service.QueryServiceFactory;

@Path("cache")
public class CacheQueryService {

  final QueryService queryService;

  public CacheQueryService() {
    super();
    System.setProperty("hazelcast.logging.type", "slf4j");
    final CachingProvider cachingProvider = Caching.getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    this.queryService = QueryServiceFactory.createService(cacheManager, null);
    this.queryService.start();
  }

  @GET
  @Path("squareRoot")
  @Produces(MediaType.APPLICATION_JSON)
  public Result squareRoot(@QueryParam("input") double input) {
    final Result result = new Result("Square Root");
    result.setInput(input);
    result.setOutput(Math.sqrt(result.getInput()));
    return result;
  }

  @GET
  @Path("square")
  @Produces(MediaType.APPLICATION_JSON)
  public Result square(@QueryParam("input") double input) {
    final Result result = new Result("Square");
    result.setInput(input);
    result.setOutput(result.getInput() * result.getInput());
    return result;
  }

  static class Result {

    double input;
    double output;
    String action;

    public Result() {}

    public Result(String action) {
      this.action = action;
    }

    public String getAction() {
      return action;
    }

    public void setAction(String action) {
      this.action = action;
    }

    public double getInput() {
      return input;
    }

    public void setInput(double input) {
      this.input = input;
    }

    public double getOutput() {
      return output;
    }

    public void setOutput(double output) {
      this.output = output;
    }
  }

}
