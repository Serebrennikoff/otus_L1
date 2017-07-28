package ru.otus.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.db_service.DBService;
import ru.otus.db_service.DBServiceImp;
import ru.otus.frontend.DBWorkSimulator;
import ru.otus.frontend.DBWorkSimulatorImp;
import ru.otus.message_system.Address;
import ru.otus.message_system.MessageSystem;
import ru.otus.message_system.MessageSystemContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class WebSocketCacheInfoServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    private DBWorkSimulator workSimulator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        MessageSystemContext messageSystemContext = (MessageSystemContext)context.getBean("messageSystemContext");
        setMessageSystem(messageSystemContext);
        super.init(config);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new CacheInfoWebSocketCreator(workSimulator));
    }

    private void setMessageSystem(MessageSystemContext messageSystemContext) {
        Address frontAddress = new Address("Frontend");
        messageSystemContext.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        messageSystemContext.setDbAddress(dbAddress);

        workSimulator = new DBWorkSimulatorImp(messageSystemContext, frontAddress);
        DBService dbService = new DBServiceImp(messageSystemContext, dbAddress);

        messageSystemContext.getMessageSystem().start();
    }
}
