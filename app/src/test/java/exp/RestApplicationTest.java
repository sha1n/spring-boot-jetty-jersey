package exp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import exp.rest.resources.TestResource;


@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.Random.class)
public class RestApplicationTest {

    private static final int EXPECTED_DOWNLOAD_BYTES = 1024 * 1024 * 10;

    @LocalServerPort
    private int randomServerPort;

    @LocalManagementPort
    private int randomManagementPort;

    @Value("${management.endpoints.web.base-path}")
    private String managementBasePath;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @DisplayName("HTTP GET test resource")
    @ParameterizedTest
    @ValueSource(strings = {"/api/test/async", "/api/test/sync"})
    public void testTestResourceTest(String path) {
        var resourceUrl = aResourceUrlWith(path);
        var entity = restTemplate.getForEntity(resourceUrl, TestResource.TestResponse.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Spring Boot Test", Objects.requireNonNull(entity.getBody()).message);
    }

    @DisplayName("HTTP GET stream download")
    @ParameterizedTest
    @ValueSource(strings = {"/api/stream/async", "/api/stream/sync"})
    public void testStreamSync(String path) {
        var resourceUrl = aResourceUrlWith(path);
        var dataBytes = restTemplate.getForObject(resourceUrl, byte[].class);

        assertEquals(dataBytes.length, EXPECTED_DOWNLOAD_BYTES);
    }

    @Test
    @DisplayName("HTTP GET management/health ")
    public void testManagementHealthResource() {
        var entity = restTemplate.getForEntity(aManagementUrlWith("/health"), String.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"UP\"}", entity.getBody());
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
