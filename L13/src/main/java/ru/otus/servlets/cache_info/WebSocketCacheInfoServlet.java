package ru.otus.servlets.cache_info;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class WebSocketCacheInfoServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        super.init(config);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new CacheInfoWebSocketCreator(context));
    }
}
