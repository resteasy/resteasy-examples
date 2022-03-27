## Usage

Start Jetty server:

```bash
$ mvn jetty:run
```

Access the service:

```bash
$ curl http://localhost:8080/resteasy/martian
Hello, Martian!
```

Access the generated WADL grammar:

```bash
$ curl http://localhost:8080/application.xml
```

Result:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<application xmlns="http://wadl.dev.java.net/2009/02">
    <resources base="http://localhost:8080/resteasy">
        <resource path="/martian">
            <param name="name" style="template" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
            <method id="input" name="POST">
                <response/>
            </method>
            <method id="hello" name="GET">
                <response/>
            </method>
            <resource path="intr/{foo}">
                <param name="foo" style="template" type="xs:int" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <method id="integerReturn" name="GET">
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="ab/{a}">
                <param name="a" style="template" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <method id="abc" name="GET">
                    <request>
                        <param name="Cookie" style="header" type="xs:int" path="b" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
        </resource>
        <resource path="/form">
            <resource path="/foo">
                <method id="foo" name="POST">
                    <request>
                        <representation mediaType="application/x-www-form-urlencoded">
                            <param name="foo" style="query" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                            <param name="bar" style="query" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        </representation>
                    </request>
                    <response/>
                </method>
            </resource>
            <resource path="/list">
                <method id="list" name="POST">
                    <request>
                        <representation mediaType="application/x-www-form-urlencoded">
                            <param name="foos" style="query" type="xs:complex" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                            <param name="maps" style="query" type="xs:complex" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        </representation>
                    </request>
                    <response/>
                </method>
            </resource>
        </resource>
        <resource path="/smoke">
            <resource path="/header">
                <method id="testHeaderParam" name="GET">
                    <request>
                        <param name="Referer" style="header" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="/header2">
                <method id="testHeaderParam2" name="GET">
                    <request>
                        <param name="Referer" style="header" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="Accept" style="header" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="mixed/{p1}/{p2}">
                <param name="p1" style="template" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <param name="p2" style="template" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <method id="mixed" name="GET">
                    <request>
                        <param name="h1" style="header" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="Cookie" style="header" type="xs:string" path="c1" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="q1" style="query" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="h2" style="header" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="Cookie" style="header" type="xs:string" path="c2" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        <param name="q2" style="query" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="matrix">
                <param name="a" style="matrix" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <param name="b" style="matrix" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                <method id="matrix" name="GET">
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="/add">
                <method id="addUser" name="POST">
                    <request>
                        <representation mediaType="application/x-www-form-urlencoded">
                            <param name="name" style="query" type="xs:string" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                            <param name="age" style="query" type="xs:int" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                        </representation>
                    </request>
                    <response/>
                </method>
            </resource>
        </resource>
    </resources>
</application>
```

## Relative Blog

- [RESTEasy WADL Grammar Support](https://resteasy.dev/2018/10/31/resteasy-wadl-grammar-support/)
