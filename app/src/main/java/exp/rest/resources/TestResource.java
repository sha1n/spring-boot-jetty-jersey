package exp.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Path("/test")
public class TestResource {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @GET
    @Path("async")
    @Produces(MediaType.APPLICATION_JSON)
    public void testAsync(@Suspended final AsyncResponse asyncResponse) {
        executorService.execute(() -> asyncResponse.resume(new TestResponse()));
    }

    @GET
    @Path("sync")
    @Produces(MediaType.APPLICATION_JSON)
    public TestResponse test() {
        return new TestResponse();
    }

    public static class TestResponse {
        public String message = "Spring Boot Test";
        public long timestamp = System.currentTimeMillis();
    }
}
