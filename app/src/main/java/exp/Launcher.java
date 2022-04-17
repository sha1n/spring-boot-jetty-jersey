package exp;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import exp.rest.ApplicationConfiguration;


/**
 * This is the Spring-Boot application launcher
 *
 * @author sha1n
 * Date: 4/13/14
 */
@ComponentScan(basePackages = {"exp.rest"})
@SpringBootApplication
public class Launcher extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Launcher.class);
    }

    @Bean
    public ServletRegistrationBean<ServletContainer> jerseyServlet() {
        var registration = new ServletRegistrationBean<>(new ServletContainer(), "/api/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, ApplicationConfiguration.class.getName());
        return registration;
    }

    @Bean
    public JettyServletWebServerFactory containerFactory() {
        final var jettyEmbeddedServletContainerFactory = new JettyServletWebServerFactory();
        jettyEmbeddedServletContainerFactory.addServerCustomizers(new JettyConfigurer());
        return jettyEmbeddedServletContainerFactory;
    }
}
