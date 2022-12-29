package exp.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import exp.rest.resources.TestResource;
import exp.rest.resources.TestStream;

/**
 * This is the Jersey Application class. Here we declare which packages contain JAX-RS resources, which Jersey features
 * are enabled etc.
 *
 * @author sha1n
 * Date: 4/13/14
 */
@ApplicationPath("/api")
public class ApplicationConfiguration extends ResourceConfig {

    public ApplicationConfiguration() {
        super();

        // Scan resources package
        // packages("exp.rest.resources");  FIXME: doens't work with SB fat jar
        register(TestResource.class);
        register(TestStream.class);

        register(JacksonFeature.class);
    }
}
