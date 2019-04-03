package org.jboss.resteasy.tracing.examples.jsonb;

import java.lang.reflect.Method;

public class Play2 {
    private static Object mapper = null;
    private static Class mapperClass = null;

    public static void main(String[] args) throws Exception {
        mapperClass = Class.forName("javax.json.bind.JsonbBuilder");
        Method method = mapperClass.getDeclaredMethod("create");
        mapper = method.invoke(null);
        System.out.println(mapper);
    }
}
