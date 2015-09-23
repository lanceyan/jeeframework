/*
 * @project: jeeframework 
 * @package: com.jeeframework.logicframework.common.remote.http
 * @title:   FlashCrossDomainServlet.java 
 *
 * Copyright (c) 2015 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.common.remote.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2015-09-21 22:57
 */
public class FlashCrossDomainServlet extends HttpServlet {

    private static Logger Log = LoggerFactory.getLogger(FlashCrossDomainServlet.class);

    public static String CROSS_DOMAIN_TEXT = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n" +
            "<cross-domain-policy>\n" +
            "\t<site-control permitted-cross-domain-policies=\"all\"/>\n" +
            "\t<allow-access-from domain=\"*\" to-ports=\"";

    public static String CROSS_DOMAIN_MIDDLE_TEXT = "\" secure=\"";
    public static String CROSS_DOMAIN_END_TEXT = "\"/>\n</cross-domain-policy>";

    private static String CROSS_DOMAIN_SECURE_ENABLED = "httpbind.crossdomain.secure";
    private static boolean CROSS_DOMAIN_SECURE_DEFAULT = true;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/xml");
        response.getOutputStream().write(getCrossDomainContent().getBytes());
    }

    /**
     * Returns the content for <tt>crossdomain.xml</tt>, either by generating
     * content, or by passing the provided file in
     * <tt>&lt;OpenfireHome&gt;/conf/crossdomain.xml</tt>
     *
     * @return content for the <tt>crossdomain.xml</tt> that should be served
     * for this service.
     */
    public static String getCrossDomainContent() {
//        final String override = getContent(getOverride());
//        if (override != null && override.trim().length() > 0) {
//            return override;
//        } else {
            return generateOutput();
//        }
    }

    /**
     * Returns <tt>&lt;OpenfireHome&gt;/conf/crossdomain.xml</tt> as a File
     * object (even if the file does not exist on the file system).
     *
     * @return <tt>&lt;OpenfireHome&gt;/conf/crossdomain.xml</tt>
     */
//    private static File getOverride() {
//        final StringBuilder sb = new StringBuilder();
//        sb.append(JiveGlobals.getHomeDirectory());
//        if (!sb.substring(sb.length() - 1).startsWith(File.separator)) {
//            sb.append(File.separator);
//        }
//        sb.append("conf");
//        sb.append(File.separator);
//
//        return new File(sb.toString(), "crossdomain.xml");
//    }

    /**
     * Return content of the provided file as a String.
     *
     * @param file The file from which to get the content.
     * @return String-based content of the provided file.
     */
//    private static String getContent(File file) {
//        final StringBuilder content = new StringBuilder();
//        if (file.canRead()) {
//            try {
//                final BufferedReader in = new BufferedReader(new FileReader(
//                        file));
//                String str;
//                while ((str = in.readLine()) != null) {
//                    content.append(str);
//                    content.append('\n');
//                }
//                in.close();
//            } catch (IOException ex) {
//                Log.warn("Unexpected exception while trying to read file: " + file.getName(), ex);
//                return null;
//            }
//        }
//
//        return content.toString();
//    }

    /**
     * Dynamically generates content for a non-restrictive <tt>crossdomain.xml</tt> file.
     */
    private static String generateOutput() {
        final StringBuilder builder = new StringBuilder();
        builder.append(CROSS_DOMAIN_TEXT);
        getPortList(builder);
        builder.append(CROSS_DOMAIN_MIDDLE_TEXT);
        getSecure(builder);
        builder.append(CROSS_DOMAIN_END_TEXT);
        builder.append("\n");

        return builder.toString();
    }

    private static StringBuilder getPortList(StringBuilder builder) {
        boolean multiple = false;
        // ports for http-binding may not be strictly needed in here, but it doesn't hurt
        if (HttpServer.getInstance().getHttpBindUnsecurePort() > 0) {
            if (multiple) {
                builder.append(",");
            }
            builder.append(HttpServer.getInstance().getHttpBindUnsecurePort());
            multiple = true;
        }

        return builder;
    }

    private static StringBuilder getSecure(StringBuilder builder) {
        builder.append("false");
        return builder;
    }


}