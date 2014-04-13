package org.springframework.boot.context.embedded.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;

/**
 * @author sha1n
 * Date: 4/13/14
 */
public class PatchedJettyEmbeddedServletContainer extends JettyEmbeddedServletContainer {


    public PatchedJettyEmbeddedServletContainer(Server server) {
        super(server, true);
    }


    @Override
    public void start() throws EmbeddedServletContainerException {
        try {
            Server server = getServer();
            server.start();
            for (Handler handler : server.getHandlers()) {
                handleDeferredInitialize(handler);
            }
            Connector[] connectors = server.getConnectors();
            for (Connector connector : connectors) {
                connector.start();
            }
        }
        catch (Exception ex) {
            throw new EmbeddedServletContainerException(
                    "Unable to start embedded Jetty servlet container", ex);
        }
    }

    private void handleDeferredInitialize(Handler handler) throws Exception {
        if (handler instanceof JettyEmbeddedWebAppContext) {
            ((JettyEmbeddedWebAppContext) handler).deferredInitialize();
        } else if (handler instanceof HandlerWrapper) {
            handleDeferredInitialize(((HandlerWrapper)handler).getHandler());
        }
    }

}
