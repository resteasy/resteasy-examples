package org.jboss.resteasy.grpc.server;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.greet.GreetServiceGrpc;
import org.greet.GreetServiceGrpc.GreetServiceBlockingStub;
import org.greet.Greet_proto.GeneralEntityMessage;
import org.greet.Greet_proto.GeneralReturnMessage;
import org.greet.Greet_proto.dev_resteasy_greet___GeneralGreeting;
import org.greet.Greet_proto.dev_resteasy_greet___Greeting;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.dmr.ModelNode;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public class GreetingTest {

    private static ManagedChannel channelPlaintext;
    private static GreetServiceBlockingStub blockingStub;

    @BeforeClass
    public static void beforeClass() throws Exception {

        // Use plaintext connection
        try (ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9990)) {
            final ModelNode handlerAddress = Operations.createAddress("subsystem", "grpc");
            final ModelNode op = Operations.createWriteAttributeOperation(handlerAddress, "key-manager-name", "");
            final ModelNode result = client.execute(op);
            if (!Operations.isSuccessfulOutcome(result)) {
                throw new RuntimeException("Failed to execute operation: " + op + " " +
                        Operations.getFailureDescription(result).asString());
            }
        }

        // Establish ServletContext
        Client client = ClientBuilder.newClient();
        String url = "http://localhost:8080/grpcToRest.example.grpc-0.0.1-SNAPSHOT/grpcToJakartaRest/grpcserver/context";
        client.target(url).request().get();
        client.close();

        // Create gRPC connection
        channelPlaintext = ManagedChannelBuilder.forTarget("localhost:9555").usePlaintext().build();
        blockingStub = GreetServiceGrpc.newBlockingStub(channelPlaintext);
    }

    @Test
    public void testGreeting() {
        GeneralEntityMessage.Builder builder = GeneralEntityMessage.newBuilder();
        GeneralEntityMessage gem = builder.setURL("http://localhost:8080/greet/Bill").build();
        try {
            GeneralReturnMessage grm = blockingStub.greet(gem);
            dev_resteasy_greet___Greeting greeting = grm.getDevResteasyGreetGreetingField();
            Assert.assertEquals("hello, Bill", greeting.getS());
        } catch (StatusRuntimeException e) {
            //
        }
    }

    @Test
    public void testGeneralGreeting() {
        GeneralEntityMessage.Builder builder = GeneralEntityMessage.newBuilder();
        GeneralEntityMessage gem = builder.setURL("http://localhost:8080/salute/Bill?salute=Heyyy").build();
        try {
            GeneralReturnMessage grm = blockingStub.generalGreet(gem);
            dev_resteasy_greet___GeneralGreeting greeting = grm.getDevResteasyGreetGeneralGreetingField();
            Assert.assertEquals("Heyyy", greeting.getSalute());
            Assert.assertEquals("Bill", greeting.getGreetingSuper().getS());
        } catch (StatusRuntimeException e) {
            //
        }
    }
}
