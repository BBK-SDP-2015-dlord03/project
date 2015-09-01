package uk.ac.bbk.dlord03.webservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class QueryServer {

  public static void main(String[] args) throws Exception {

    ServletContextHandler context;
    context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    Server server;
    server = new Server(8080);
    server.setHandler(context);

    ServletHolder servletHolder;
    servletHolder = context.addServlet(ServletContainer.class, "/*");
    servletHolder.setInitOrder(0);
    servletHolder.setInitParameter("jersey.config.server.provider.classnames",
      CacheQueryService.class.getCanonicalName());

    server.start();
    System.out.println("Server started.\nPress enter to stop...");
    System.in.read();
    server.stop();

  }
}
