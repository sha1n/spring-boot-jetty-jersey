package exp;

import exp.rest.resources.TestResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * @author sha1n
 * Date: 4/13/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Launcher.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext
public class RestApplicationTest {

    @Value("${management.address}")
    String managementAddress;

    @Value("${management.port}")
    int managementPort;

    @Value("${server.address}")
    String serverAddress;

    @Value("${server.port}")
    int serverPort;

    String serverBaseUrl() {
        return "http://" + serverAddress + ":" + serverPort;
    }

    String managementBaseUrl() {
        return "http://" + managementAddress + ":" + managementPort;
    }

    @Test
    public void testTestResourceTest() throws Exception {
        doTest(serverBaseUrl() + "/api/test");
    }

    @Test
    public void testTestResourceTestAsync() throws Exception {
        doTest(serverBaseUrl() + "/api/test/async");
    }

    @Test
    public void testHealthResource() throws Exception {
        ResponseEntity<String> entity =
                new TestRestTemplate().getForEntity(managementBaseUrl() + "/health", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("{\"status\":\"UP\"}", entity.getBody());
    }

    private void doTest(String url) {
        ResponseEntity<TestResource.Resp> entity =
                new TestRestTemplate().getForEntity(url, TestResource.Resp.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Spring Boot Test", entity.getBody().message);
    }
}
