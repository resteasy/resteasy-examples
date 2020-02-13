import org.jboss.resteasy.examples.springundertow.Main;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SmokeTest {

    @Test(expected = Exception.class)
    public void test() throws Exception {
        runWithServer(() -> {
            Client client = ClientBuilder.newClient();

            // Access server and get expected result
            Assert.assertEquals("Hello, world!",
                    client
                            .target("http://localhost:8081/rest/foo")
                            .request()
                            .get(String.class));
        });

    }

    private void runWithServer(Runnable test) throws Exception {
        // Start Server
        Future future = CompletableFuture.runAsync(() -> {
            try {
                Main.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(2000);

        test.run();

        // Force Shutdown Server
        future.get(1, TimeUnit.SECONDS);
    }
}
