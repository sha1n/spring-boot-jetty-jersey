package exp.rest.resources;

import org.apache.commons.io.IOUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Path("/stream")
public class TestStream {

    @Path("/async")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void streamAsync(@Suspended final AsyncResponse asyncResponse,
                            @Context HttpServletRequest servletRequest) throws IOException {
        assert servletRequest.isAsyncStarted();

        final AsyncContext asyncContext = servletRequest.getAsyncContext();
        final ServletResponse response = asyncContext.getResponse();
        final int bufferSize = response.getBufferSize();
        final ServletOutputStream servletOutputStream = response.getOutputStream();
        final InputStream randomInputStream = random10MegStream();

        WriteListener writeHandler = new WriteListener() {
            volatile boolean done = false;
            final byte[] buffer = new byte[bufferSize];

            public void onWritePossible() throws IOException {
                while (servletOutputStream.isReady() && !done) {
                    servletOutputStream.write(buffer, 0, randomInputStream.read(buffer));
                    done = randomInputStream.available() == 0;
                    if (done) {
                        asyncContext.complete();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                asyncContext.complete();
                throwable.printStackTrace();
            }
        };


        servletOutputStream.setWriteListener(writeHandler);
    }

    @Path("/sync")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void streamSync(@Context HttpServletResponse servletResponse) throws IOException {

        ServletOutputStream servletOutputStream = servletResponse.getOutputStream();
        InputStream is = random10MegStream();
        IOUtils.copy(is, servletOutputStream, servletResponse.getBufferSize());
    }

    private InputStream random10MegStream() {
        byte[] data = new byte[1024 * 1024 * 10];
        new Random().nextBytes(data);
        return new ByteArrayInputStream(data);
    }

}
