package exp;

import exp.rest.resources.TestResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * @author sha1n
 * Date: 4/13/14
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestApplicationTest {

    private static final int EXPECTED_DOWNLOAD_BYTES = 1024 * 1024 * 10;

    @Value("${management.server.port}")
    int managementPort;

    @Value("${management.endpoints.web.base-path}")
    String managementBasePath;

    @Value("${server.address}")
    String serverAddress;

    @Value("${server.port}")
    int serverPort;

    private String serverBaseUrl() {
        return "http://" + serverAddress + ":" + serverPort;
    }

    private String managementBaseUrl() {
        return "http://" + serverAddress + ":" + managementPort + managementBasePath;
    }

    @Test
    public void testTestResourceTest() {
        doTest(serverBaseUrl() + "/api/test");
    }

    @Test
    public void testTestResourceTestAsync() {
        doTest(serverBaseUrl() + "/api/test/async");
    }

    @Test
    public void testHealthResource() {
        ResponseEntity<String> entity =
                new TestRestTemplate().getForEntity(managementBaseUrl() + "/health", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"UP\"}", entity.getBody());
    }

    @Test
    public void testStreamSync() {
        testDownload("/api/stream/sync");
    }

    @Test
    public void testStreamAsync() {
        testDownload("/api/stream/async");
    }

    private void testDownload(String path) { // only checking stream length...
        byte[] dataBytes = new TestRestTemplate().getForObject(serverBaseUrl() + path, byte[].class);

        assertEquals(dataBytes.length, EXPECTED_DOWNLOAD_BYTES);
    }

    private void doTest(String url) {
        ResponseEntity<TestResource.Resp> entity =
                new TestRestTemplate().getForEntity(url, TestResource.Resp.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Spring Boot Test", Objects.requireNonNull(entity.getBody()).message);
    }
}
