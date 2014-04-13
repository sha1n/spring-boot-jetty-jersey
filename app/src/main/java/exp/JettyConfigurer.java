package exp;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * This is a JettyServerCustomizer implementation which configures Jetty's WebAppContext from a standard Jetty xml
 * configuration file.
 *
 * @author sha1n
 * Date: 4/13/14
 */
public class JettyConfigurer implements JettyServerCustomizer {

    @Override
    public void customize(Server server) {
        WebAppContext webAppContext = (WebAppContext) server.getHandler();
        try {
            // Load configuration from resource file (standard Jetty xml configuration) and configure the context.
            createConfiguration("/etc/jetty.xml").configure(webAppContext);
            createConfiguration("/etc/jetty-rewrite.xml").configure(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private XmlConfiguration createConfiguration(String xml) throws IOException, SAXException {
        return new XmlConfiguration(Launcher.class.getResourceAsStream(xml));
    }
}
