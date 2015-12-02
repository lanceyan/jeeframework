package com.jeeframework.util.properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JeePropertiesTest {


    @Test
    public void testGetProperty() throws Exception {
        JeeProperties props = new JeeProperties(
                "http.ini");
        assertEquals("127.0.0.1", props.getProperty("http.host"));
        assertEquals("80", props.getProperty("http.port"));
        assertNull(props.getProperty("test"));
        assertNull(props.getProperty("nothing.something"));
    }


}
