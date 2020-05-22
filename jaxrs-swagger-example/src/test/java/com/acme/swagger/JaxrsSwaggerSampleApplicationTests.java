package com.acme.swagger;

import com.acme.swagger.rest.DummyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JaxrsSwaggerSampleApplicationTests {

    @Test
    void dummyTest() {
        DummyService dummyService = new DummyService();
        String res = dummyService.get();
        assertEquals("dummy", res);
    }

}
