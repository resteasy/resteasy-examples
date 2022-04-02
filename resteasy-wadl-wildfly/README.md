# An example showing how to deploy resteasy-wadl based project to Wildfly

## USAGE

This example has `wildfly-maven-plugin` embedded, so it can download Wildfly server by itself, and start the server also deploying the compiled example into server.

To do so, run the following command:

```bash
$ mvn wildfly:run
```

And the Wildfly server is started and also the example is deployed:

```bash
22:17:29,885 INFO  [org.jboss.as.repository] (management-handler-thread - 1) WFLYDR0001: Content added at location /Users/weli/works/resteasy-examples/resteasy-wadl-wildfly/target/wildfly-preview-26.0.1.Final/standalone/data/content/0c/ec54abd4af172d323f27bc1a313d206de84f99/content
22:17:29,919 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "resteasy-wadl-wildfly.war" (runtime-name: "resteasy-wadl-wildfly.war")
22:17:30,950 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000128: Infinispan version: Infinispan 'Taedonggang' 12.1.7.Final
22:17:30,997 INFO  [org.infinispan.CONFIG] (MSC service thread 1-3) ISPN000152: Passivation configured without an eviction policy being selected. Only manually evicted entities will be passivated.
22:17:30,998 INFO  [org.infinispan.CONFIG] (MSC service thread 1-3) ISPN000152: Passivation configured without an eviction policy being selected. Only manually evicted entities will be passivated.
22:17:31,055 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000556: Starting user marshaller 'org.wildfly.clustering.infinispan.spi.marshalling.InfinispanProtoStreamMarshaller'
22:17:31,152 INFO  [org.infinispan.CONTAINER] (ServerService Thread Pool -- 78) ISPN000025: wakeUpInterval is <= 0, not starting expired purge thread
22:17:31,172 INFO  [org.jboss.as.clustering.infinispan] (ServerService Thread Pool -- 78) WFLYCLINF0002: Started http-remoting-connector cache from ejb container
22:17:31,985 INFO  [org.jboss.resteasy.resteasy_jaxrs.i18n] (ServerService Thread Pool -- 78) RESTEASY002225: Deploying jakarta.ws.rs.core.Application: class org.jboss.resteasy.examples.wadl.wildfly.MyApplication
22:17:32,059 INFO  [org.hibernate.validator.internal.util.Version] (ServerService Thread Pool -- 78) HV000001: Hibernate Validator 7.0.1.Final
22:17:32,102 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 78) WFLYUT0021: Registered web context: '/resteasy-wadl-wildfly' for server 'default-server'
22:17:32,141 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "resteasy-wadl-wildfly.war" (runtime-name : "resteasy-wadl-wildfly.war")
```

Now we can use `curl` command to access the WADL information of the example:

```bash
$ curl "localhost:8080/resteasy-wadl-wildfly/rest/application.xml"
```

And server will return the WADL infomation:

```bash
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<application xmlns="http://wadl.dev.java.net/2009/02">
    <grammars>
        <include href="xsd0.xsd">
            <doc title="Generated" xml:lang="en"/>
        </include>
    </grammars>
    <resources base="/">
        <resource path="/foo">
            <method id="post" name="POST">
                <response>
                    <representation mediaType="application/xml"/>
                </response>
            </method>
        </resource>
        <resource path="/">
            <resource path="/application.xml">
                <method id="output" name="GET">
                    <response>
                        <representation mediaType="application/xml"/>
                    </response>
                </method>
            </resource>
            <resource path="/wadl-extended/{path}">
                <param name="path" style="template" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <method id="grammars" name="GET">
                    <response>
                        <representation mediaType="application/xml"/>
                    </response>
                </method>
            </resource>
        </resource>
    </resources>
</application>
```

Next we can try to access the GRAMMAR information:

```bash
$ curl "localhost:8080/resteasy-wadl-wildfly/rest/wadl-extended/xsd0.xsd"
<?xml version="1.0" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <xs:element name="listType" type="listType"/>

  <xs:complexType name="listType">
    <xs:sequence>
      <xs:element name="values" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
    </xs:sequence>
  </xs:complexType>
</xs:schema>
```

From above example we can see the WADL module is working under Wildfly deployment situation.
