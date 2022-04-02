Async Job Service
=================

This project is a simple example showing how to use the Asynchronous Job Service. 

Building the project:
------------------------

2. Run the following command to run the test for demo:

```bash
$ mvn test
```

And you will see some output like following:

```bash
Running org.jboss.resteasy.examples.asyncjob.AsyncJobTest
jobUrl1: http://localhost:9095/asyncjobs/1648047408072--317359366
IN PUT!!!!
******* countdown complete ****
```

3. To start the demo service, run the following command:

```bash
$ mvn jetty:run 
```

This will build a WAR and run it with embedded Jetty