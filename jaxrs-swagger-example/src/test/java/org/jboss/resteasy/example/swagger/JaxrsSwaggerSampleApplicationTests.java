package org.jboss.resteasy.example.swagger;

import org.jboss.resteasy.example.swagger.common.HttpWebResponse;
import org.jboss.resteasy.example.swagger.rest.DummyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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
