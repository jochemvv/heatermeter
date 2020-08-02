package nl.tabitsolutions.heatermeter;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class HealthTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {  server.stop(); }
        if (client != null) {  client.stop(); }
    }

    @Test
    public void checkHealth() {
        HttpRequest request = HttpRequest.GET("/heater-meter/health");
        HttpResponse<String> resp = client.toBlocking().exchange(request, String.class);

        assertThat(resp.getBody()).contains("up");
    }
}