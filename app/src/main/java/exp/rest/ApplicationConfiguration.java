package exp.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

import javax.ws.rs.ApplicationPath;

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

        // Create a recursive package scanner
        PackageNamesScanner resourceFinder = new PackageNamesScanner(new String[]{"exp.rest.resources"}, true);
        // Register the scanner with this Application
        registerFinder(resourceFinder);
        register(JacksonFeature.class);
    }
}
