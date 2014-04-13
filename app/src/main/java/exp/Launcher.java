package exp;

import exp.rest.ApplicationConfiguration;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.PatchedJettyEmbeddedServletContainer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This is the Spring-Boot application launcher
 *
 * @author sha1n
 * Date: 4/13/14
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"exp.rest"})
public class Launcher extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Launcher.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Launcher.class);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/api/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, ApplicationConfiguration.class.getName());
        return registration;
    }

    @Bean
    public EmbeddedServletContainerFactory containerFactory() {
        final JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory = new JettyEmbeddedServletContainerFactory() {
            @Override
            protected JettyEmbeddedServletContainer getJettyEmbeddedServletContainer(Server server) {
                return new PatchedJettyEmbeddedServletContainer(server);
            }
        };
        jettyEmbeddedServletContainerFactory.addServerCustomizers(new JettyConfigurer());
        return jettyEmbeddedServletContainerFactory;
    }
}
