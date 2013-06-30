package org.peercloud.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * User: Stanislav Ovsyannikov
 * Date: 6/26/13
 * Time: 7:18 PM
 */
public class WebServer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    //@Resource
    private String webappsHome = ".";

    private Server server;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    protected void init() {
        server = new Server(9999);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/admin");
        webapp.setWar(webappsHome+"/webapps/admin.war");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webapp });

        server.setHandler(contexts);

        try {
            server.start();
        } catch (Exception e) {
            logger.error("failed to deploy admin application", e);
        }
    }
}
