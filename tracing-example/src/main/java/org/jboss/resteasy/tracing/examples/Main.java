package org.jboss.resteasy.tracing.examples;

public class Main {


    public static void main(String[] args) throws Exception {


        Demo.buildServer();
        Thread.currentThread().join(); // keep server running...

    }
}
