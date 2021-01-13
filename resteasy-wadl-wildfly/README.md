# An example showing how to deploy resteasy-wadl based project to Wildfly

## USAGE

To deploy this sample project into Wildfly, firstly you need to replace the embedded Wildfly RESTEasy module with the
RESTEasy provided one. To do this, please refer to this section of the RESTEasy document:

* [Upgrading RESTEasy within WildFly](https://docs.jboss.org/resteasy/docs/4.5.8.Final/userguide/html_single/index.html#upgrading-wildfly)

It is recommended to use the same version of the RESTEasy dependency of this project. After replacing the RESTEasy
module in Wildfly, the next step is to package this project with the following Maven command:

```bash
$ mvn package
```

After above task is done, we will get an `resteasy-wadl-wildfly.war` in `target` directory:

```bash
$ ls target/                                                                                                                                                                                                                    22:41:24
classes/                   generated-sources/         maven-archiver/            maven-status/              resteasy-wadl-wildfly/     resteasy-wadl-wildfly.war
$  
```

We can use this WAR file to deploy to Wildfly. To test using it, firstly start the Wildfly in standalone mode:

```bash
$ ./standalone.sh
...
```

After Wildfly server is started, using its `jboss-cli.sh` admin tool to connect to server:

```bash
$ ./jboss-cli.sh
You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
[disconnected /] connect localhost
[standalone@localhost:9990 /]
```

After connected to Wildfly server, we can deploy our WAR file to the server will following command:

```bash
[standalone@localhost:9990 /] deploy <your_example_path>/resteasy-wadl-wildfly.war
```

And from server output we can see the WAR is deployed:

```bash
23:01:53,637 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "resteasy-wadl-wildfly.war" (runtime-name : "resteasy-wadl-wildfly.war")
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
