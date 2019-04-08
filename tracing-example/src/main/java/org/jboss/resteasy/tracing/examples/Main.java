package org.jboss.resteasy.tracing.examples;

public class Main {


    public static void main(String[] args) throws Exception {


        Demo.buildServer();
        System.out.println("Server started.");
        Thread.currentThread().join(); // keep server running...

    }
}
