package com.jeeframework.util.xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XMLPropertiesTest {

    @Test
    public void testAttributes() throws Exception {
        String aaa = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        String xml = "<root><foo></foo></root>";
        XMLProperties props = new XMLProperties(new ByteArrayInputStream(xml.getBytes()));
        assertNull(props.getAttribute("foo", "bar"));
        xml = "<root><foo bar=\"test123\"></foo></root>";
        props = new XMLProperties(new ByteArrayInputStream(xml.getBytes()));
        assertEquals(props.getAttribute("foo", "bar"), "test123");
    }

    @Test
    public void testGetProperty() throws Exception {
        XMLProperties props = new XMLProperties(
                "database.xml");
        assertEquals("123", props.getProperty("foo.bar"));
        assertEquals("456", props.getProperty("foo.bar.baz"));
        assertNull(props.getProperty("foo"));
        assertNull(props.getProperty("nothing.something"));
    }

    @Test
    public void testGetPropertyWithXMLEntity() throws Exception {
        String xml = "<root><foo>foo&amp;bar</foo></root>";
        XMLProperties props = new XMLProperties(new ByteArrayInputStream(xml.getBytes()));
        assertEquals("foo&bar", props.getProperty("foo"));
    }
}
