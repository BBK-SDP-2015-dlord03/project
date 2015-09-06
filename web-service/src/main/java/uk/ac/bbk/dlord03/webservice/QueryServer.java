package uk.ac.bbk.dlord03.webservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryServer {

  private static final Logger LOG = LoggerFactory.getLogger(QueryServer.class);

  public static void main(String[] args) throws Exception {

    ServletContextHandler context;
    context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addEventListener(new ServiceLifecycleManager());

    Server server;
    server = new Server(8080);
    server.setHandler(context);

    ServletHolder servletHolder;
    servletHolder = context.addServlet(ServletContainer.class, "/*");
    servletHolder.setInitOrder(0);
    servletHolder.setInitParameter("jersey.config.server.provider.classnames",
          CacheQueryService.class.getCanonicalName());

    server.start();
    server.setStopAtShutdown(true);
    LOG.info("Server started.\nPress enter to stop...");
    System.in.read();
    server.stop();
    System.exit(0);

  }
}
