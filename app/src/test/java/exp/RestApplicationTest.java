package exp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import exp.rest.resources.TestResource;

/**
 * @author sha1n
 * Date: 4/13/14
 */
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApplicationTest {

    private static final int EXPECTED_DOWNLOAD_BYTES = 1024 * 1024 * 10;

    @LocalServerPort
    private int randomServerPort;

    @LocalManagementPort
    private int randomManagementPort;

    @Value("${management.endpoints.web.base-path}")
    private String managementBasePath;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("HTTP GET test resource sync")
    public void testTestResourceTest() {
        testGetTestResourceFrom("/api/test");
    }

    @Test
    @DisplayName("HTTP GET test resource async")
    public void testTestResourceTestAsync() {
        testGetTestResourceFrom("/api/test/async");
    }

    @Test
    @DisplayName("HTTP GET management/health ")
    public void testManagementHealthResource() {
        var entity = restTemplate.getForEntity(aManagementUrlWith("/health"), String.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"UP\"}", entity.getBody());
    }

    @Test
    @DisplayName("HTTP GET download sync")
    public void testStreamSync() {
        testDownloadResourceFrom("/api/stream/sync");
    }

    @Test
    @DisplayName("HTTP GET download async")
    public void testStreamAsync() {
        testDownloadResourceFrom("/api/stream/async");
    }

    private void testDownloadResourceFrom(String path) { // only checking stream length...
        var resourceUrl = aResourceUrlWith(path);
        var dataBytes = restTemplate.getForObject(resourceUrl, byte[].class);

        assertEquals(dataBytes.length, EXPECTED_DOWNLOAD_BYTES);
    }

    private void testGetTestResourceFrom(String path) {
        var resourceUrl = aResourceUrlWith(path);
        var entity = restTemplate.getForEntity(resourceUrl, TestResource.TestResponse.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Spring Boot Test", Objects.requireNonNull(entity.getBody()).message);
    }

    private String aManagementUrlWith(String path) {
        return this.aServerUrlWith(randomManagementPort, managementBasePath + path);
    }

    private String aResourceUrlWith(String path) {
        return this.aServerUrlWith(randomServerPort, path);
    }

    private String aServerUrlWith(int port, String path) {
        return String.format("http://localhost:%d%s", port, path);
    }
}
