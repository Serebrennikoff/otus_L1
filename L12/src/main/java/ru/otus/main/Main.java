package ru.otus.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.DBService;
import ru.otus.db_service.DBServiceImp;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.servlets.LoginServlet;
import ru.otus.servlets.WebSocketCacheInfoServlet;

/**
 * ...
 */
public class Main {
    private final static int PORT = 8090;
    private final static String SOURCE_FOLDER = "L12/public_html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resHandler = new ResourceHandler();
        resHandler.setResourceBase(SOURCE_FOLDER);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(WebSocketCacheInfoServlet.class, "/cache-info");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resHandler, context));

        server.start();
        server.join();
    }
}
