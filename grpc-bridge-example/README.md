# gRPC Bridge Example

gRPC ([https://grpc.io/](https://grpc.io/)), like Jakarta RESTFul Web Services
([https://jakarta.ee/specifications/restful-ws/](https://jakarta.ee/specifications/restful-ws/)),
is a framework for client-server invocations. The semantics,
however, are quite different, and, normally, they are two distinct and non-communicating worlds. However,
the RESTEasy [gRPC Bridge](https://github.com/resteasy/resteasy-grpc) project provides a mechanism for creating
a bridge between the two worlds. In particular, gRPC Bridge can take an existing Jakarta RESTful Web Services
(Jakarta REST, for short) project (the **target** project) and extend it to a **bridge** project which contains

1. the contents of the target project, and
2. generated classes that constitute a layer that dispatches a gRPC client invocation to a Jakarta REST
  service.
  
Note that 1 implies that the bridge project can still function as a Jakarta REST project.

In this example, we will walk though the process of building a bridge project, using this project as
the target project. There are three classes:

1. `Greeting`: a simple message type
2. `GeneralGreeting`: a message type derived from `Greeting`
3. `Greeter`: a Jakarta REST resource class with two resource methods

**Note.**
We assume a basic understanding of Jakarta REST, gRPC, and [protobuf](https://protobuf.dev/), the latter
being the data foundation of gRPC.

## Introduction

A gRPC application begins with a .proto file, a language independent description of message types and
procedure calls, which can be compiled into a number of supported programming languages. In our context,
however, we are assuming the prior existence of a Jakarta REST application. The first step, then, in
creating a gRPC bridge application is to parse a set of resource classes and generate a matching .proto file.
When the .proto file is compiled into Java, the result is a set of classes representing the original
classes. For each original class, we call the compiled class its **javabuf** representation. The gRPC
infrastructure supports translating javabuf classes to and from a wire representation, so a gRPC client
can send a javabuf instance, and a reconstituted javabuf instance will appear on the server side. Normally,
the javabuf instance would be dispatched to a gRPC service, but, in our context, we want to dispatch it
to a Jakarta REST service. As we will see below, the gRPC bridge project supplies the mechanism for
translating a javabuf instance to its corresponding original Java class and dispatching it appropriately.

**Note.**
In this example we refer to the grpc-bridge-example in https://github.com/resteasy/resteasy-examples.
Its output is grpcToRest.example-1.0.1.Final-SNAPSHOT.war, but the WAR is not deployed to any repository. That is,
if you want to follow along, you need to download resteasy-examples and build it in the usual maven manner.

## Building the bridge project

1. Create a directory for the bridge project. We will refer to it as BUILD_HOME.

2. Use the archetype [gRPCtoJakartaREST-archetype](https://github.com/resteasy/gRPCtoJakartaREST-archetype) to create the
skeleton of the bridge project:

    <pre><code>
        mvn archetype:generate -B \
            -DarchetypeGroupId=dev.resteasy.grpc \
            -DarchetypeArtifactId=gRPCtoJakartaREST-archetype \
            -DarchetypeVersion=1.0.0.Alpha5 \
            -DgroupId=dev.resteasy.examples \
            -DartifactId=grpcToRest.example \
            -Dversion=1.0.1.Final-SNAPSHOT \
            -Dgenerate-prefix=Greet \
            -Dgenerate-package=org.greet \
            -Dresteasy-version=6.2.4.Final \
            -Dgrpc-bridge-version=1.0.0.Alpha2
    </pre></code>

    The parameters groupId, artifactId, and version describe the target project. 
    The result will be a new maven project, dev.resteasy.examples:grpcToRest.example.grpc:1.0.1.Final-SNAPSHOT in
    a directory named grpcToRest.example (the value of artifactId). Note that the groupId and version are copied
    from the target project, and ".grpc" is added to the artifactId. The parameters generate-prefix and generate-package are
    applied to several classes that will be generated.

    The resulting skeleton bridge project has contents:

    <pre><code>
       grpcToRest/pom.xml
       grpcToRest/src/main/resources/buildjar
       grpcToRest/src/main/resources/deployjar
       grpcToRest/src/main/webapp/META-INF/beans.xml
       grpcToRest/src/main/webapp/WEB-INF/web.xml
    </code></pre>

3. Build the bridge project:

<pre><code>
   mvn clean install 
</code></pre>

A number of things happen:

* The Java classes are copied from the target project:
    
        src/main/java/dev/resteasy/example/grpc/greet/GeneralGreeting.java
        src/main/java/dev/resteasy/example/grpc/greet/Greeter.java
        src/main/java/dev/resteasy/example/grpc/greet/Greeting.java

* A .proto file is created to describe the gRPC message types and procedure calls:
    
        src/main/proto/Greet.proto

Let's take a look at Greet.proto. The two message types are transformed into 

        message dev_resteasy_example_grpc_greet___Greeting {
          string s = 1;
        }

        message dev_resteasy_example_grpc_greet___GeneralGreeting {
          string salute = 1;
          dev_resteasy_example_grpc_greet___Greeting greeting___super = 2;
        }

There are two things to note:

    1. protobuf doesn't have a concept of packages, so we represent package names with '_' characters.
    2. protobuf doesn't have a concept of inheritance, so we use a specially named member (ending in `___super`) to represent a parent class.

Going back to `Greet.proto`, let's also look at the method calls derived from `Greeter`. First, note that there are
three methods in `Greeter` but only two rpc entries in `Greet.proto`. That's because, lacking annotations, 
`getGeneralGreeting()` is not a Jakarta REST resource method. Also, note the presence of two types, `GeneralEntityMessage` and
`GeneralReturnMessage`, which do not come from the original classes:

<pre><code>
    rpc greet (GeneralEntityMessage) returns (GeneralReturnMessage);
    rpc generalGreet (GeneralEntityMessage) returns (GeneralReturnMessage);
</code></pre>

These general purpose types are needed to bridge part of the gap between the gRPC and Jakarta REST semantics.
Whereas gRPC method calls support only a single parameter, Jakarta REST, in addition to an entity parameter,
also has path parameters, query parameters, etc., and those are accomodated in `GeneralEntityMessage`.
    
* Six Java classes are generated, which constitute the intermediate layer between gRPC and the target
project:

    <pre><code>
        target/generated-sources/protobuf/java/org/greet/Greet_proto.java
        target/generated-sources/protobuf/grpc-java/org/greet/GreetServiceGrpc.java
        target/generated-sources/protobuf/grpc-java/org/greet/GreetServiceGrpcImpl.java
        target/generated-sources/protobuf/grpc-java/org/greet/Greet_Server.java
        target/generated-sources/protobuf/grpc-java/org/greet/GreetJavabufTranslator.java
        target/generated-sources/protobuf/grpc-java/org/greet/GreetMessageBodyReaderWriter.java
    </code></pre>
        
    1. `Greet_proto`: This is the compiled version of Greet.proto. The javabuf classes are defined here.
    2. `GreetServiceGrpc`: This class, generated by the protoc compiler, has a stub method for each procedure call in `Greet.proto`.
    3. `GreetServiceGrpcImpl`: Each method in `GreetServiceGrpc` is overridden here with code that creates a servlet environment and dispatches
       invocation to a Jakarta REST resource method.
    4. `Greet_Server`: A Jakarta REST resource class, discussed below.
    5. `GreetJavabufTranslator`: This tranlates back and forth between javabuf classes and their corresponding original classes. 
    6. `GreetMessageBodyReaderWriter`: This implements the Jakarta REST `MessageBodyReader` and `MessageBodyWriter` interfaces.

* A WAR ready to be deployed is generated, in this case called grpcToRest.example.grpc-0.0.1-SNAPSHOT.war.

## Using the bridge project

The easiest way of using the bridge project is to deploy the WAR to a version of WildFly supplied with RESTEasy and the gRPC subsystem,
both of which are available as WildFly feature packs. A suitable WildFly can be built as follows:

<pre><code>
galleon.sh install wildfly:current --dir=wildfly
galleon.sh install org.jboss.resteasy:galleon-feature-pack:6.2.4.Final --dir=wildfly --ignore-not-excluded-layers=true
galleon.sh install org.wildfly.extras.grpc:wildfly-grpc-feature-pack:0.1.1.Final --layers=grpc --dir=wildfly
</code></pre>

Once WildFly is available, the WAR can be deployed, and invocations may be made from the client. However, there is one preliminary 
step that is necessary. `GreetServiceGrpcImpl` creates a servlet environment in which a Jakarta REST resource method can run, and part
of that process involves supplying a `jakarta.servlet.ServletContext`. Making a native Jakarta REST call on the `startContext()` method of
`org.greet.Greet_Server` supplied in the bridge project will accomplish that. For example,

<pre><code>
    Client client = ClientBuilder.newClient();
    client.target("http://localhost:8080/grpcToRest.example.grpc-1.0.1.Final-SNAPSHOT/grpcToJakartaRest/grpcserver/context").request().get();
</code></pre>

Now, let's consider the client side. The client side code is targeted at the gRPC runtime:

<pre><code>
    @Test
    public void testGeneralGreeting() {
        GeneralEntityMessage.Builder builder = GeneralEntityMessage.newBuilder();
        GeneralEntityMessage gem = builder.setURL("http://localhost:8080/salute/Bill?salute=Heyyy").build();
        try {
           GeneralReturnMessage grm = blockingStub.generalGreet(gem);
           dev_resteasy_example_grpc_greet___GeneralGreeting greeting = grm.getDevResteasyExampleGrpcGreetGeneralGreetingField();
           Assert.assertEquals("Heyyy", greeting.getSalute());
           Assert.assertEquals("Bill", greeting.getGreetingSuper().getS());
        } catch (StatusRuntimeException e) {
           //
        } 
    }
</code></pre>

Note that the URL host and port are ignored by `Greeter.generalGreet()`, so we just use "localhost:8080" as
a placeholder.

This example comes with a test you can run, "src/main/java/GreetingTest", but it's not called "GreetingTest.java" because it depends on classes 
that will be created in grpcToRest.example.grpc. To run it, rename it, move it to src/test/java, and add two dependencies to the pom.xml:

<pre><code>
      &lt;dependency&gt;
          &lt;groupId&gt;io.grpc&lt;/groupId&gt;
          &lt;artifactId&gt;grpc-netty-shaded&lt;/artifactId&gt;
          &lt;version&gt;1.54.1&lt;/version&gt;
      &lt;/dependency&gt;
      &lt;dependency&gt;
          &lt;groupId&gt;junit&lt;/groupId&gt;
          &lt;artifactId&gt;junit&lt;/artifactId&gt;
          &lt;version&gt;4.13.2&lt;/version&gt;
      &lt;/dependency&gt;
      &lt;dependency&gt;
          &lt;groupId&gt;org.jboss.resteasy&lt;/groupId&gt;
          &lt;artifactId&gt;resteasy-client&lt;/artifactId&gt;
          &lt;version&gt;6.2.3.Final&lt;/version&gt;
      &lt;/dependency&gt;
      &lt;dependency&gt;
          &lt;groupId&gt;org.wildfly.core&lt;/groupId&gt;
          &lt;artifactId&gt;wildfly-controller-client&lt;/artifactId&gt;
          &lt;version&gt;22.0.0.Beta1-SNAPSHOT&lt;/version&gt;
      &lt;/dependency&gt;
</code></pre>

## Exploring further

1. The "gRPC Bridge" chapter in the RESTEasy User Guide [https://resteasy.dev/docs/](https://resteasy.dev/docs/) has more detailed information.
2. `org.jboss.resteasy.test.grpc.AbstractGrpcToJakartaRESTTest` in the grpc-bridge project has a lot of code demonstrating a variety
   of situations.
