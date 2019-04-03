package org.jboss.resteasy.tracing.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;

public class Play {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Car car = new Car("yellow", "renault");
        objectMapper.writeValue(System.out, car);

        Class mapperClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
        Object mapper = mapperClass.newInstance();
        Method method = mapperClass.getDeclaredMethod("writeValueAsString", Object.class);
        method.setAccessible(true);
        System.out.println(method.invoke(mapper, car));


    }
}
