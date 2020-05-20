package com.acme.swagger;

import com.acme.swagger.common.HttpWebResponse;
import com.acme.swagger.rest.DummyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JaxrsSwaggerSampleApplicationTests {

    @Test
    void dummyTest() {
        DummyService dummyService = new DummyService();
        HttpWebResponse res = dummyService.get();
        assertTrue(res.isSuccess());
        assertEquals("dummy", res.getMessage());
        assertNull(res.getResult());
    }

}
