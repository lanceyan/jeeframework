package com.jeeframework.logicframework.util.server.http;


import com.jeeframework.core.context.support.SpringContextHolder;
import com.jeeframework.core.exception.BaseException;
import com.jeeframework.logicframework.util.logging.LoggerUtil;
import com.jeeframework.logicframework.util.server.JeeFrameWorkServer;
import com.jeeframework.logicframework.util.server.WebServerUtil;
import com.jeeframework.util.string.StringUtils;
import com.jeeframework.util.validate.Validate;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class HttpServer {

    public static final String HTTP_BIND_HOST = "http.host";
    public static final String HTTP_BIND_PORT = "http.port";


    public static final String HTTP_BIND_HOST_DEFAULT = "0.0.0.0";
    public static final int HTTP_BIND_PORT_DEFAULT = 8080;


    private static final Logger Log = LoggerFactory.getLogger(HttpServer.class);

    private ContextHandlerCollection contexts;

    // is all orgin allowed flag
    private boolean allowAllOrigins;

    private int bindPort;

    private Server httpBindServer;
    private Connector httpConnector;


    private String webroot = null; //web根目录

    /**
     * 获取web程序根目录
     *
     * @return
     */
    public String getWebroot() {
        return webroot;
    }

    public HttpServer() {

        // we need to initialise contexts at constructor time in order for plugins to add their contexts before start()
        contexts = new ContextHandlerCollection();


        String path = WebServerUtil.findWebDescriptor();
        if (Validate.isEmpty(path)) {
            throw new BaseException("没有找到web.xml");
        }
        webroot = StringUtils.substringBefore(path, "WEB-INF");
    }

    public int getHttpBindUnsecurePort() {
        return JeeFrameWorkServer.getInstance().getServerProperties().getIntProperty(HTTP_BIND_PORT, HTTP_BIND_PORT_DEFAULT);
    }

    public void start() {
        ServletContextHandler context = createWebAppContext();

        bindPort = getHttpBindUnsecurePort();
        configureHttpBindServer(bindPort);

        try {
            httpBindServer.start();

            ServletContext servletContext = context.getServletContext();
            ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

            if (applicationContext == null) {
                Log.error("Web应用中没有配置spring，初始化applicationContext失败！");
            }
            else {
                SpringContextHolder.setApplicationContextByStatic(applicationContext);
            }

            Log.info("HTTP bind service started");
            LoggerUtil.infoTrace("************************   http server started ************************");
        } catch (Exception e) {
            Log.error("Error starting HTTP bind service", e);
        }
    }

    public void stop() {
        if (httpBindServer != null) {
            try {
                httpBindServer.stop();
                Log.info("HTTP bind service stopped");
            } catch (Exception e) {
                Log.error("Error stopping HTTP bind service", e);
            }
            httpBindServer = null;
        }
    }

    private ServletContextHandler createWebAppContext() {
        ServletContextHandler context;
        // Add web-app. Check to see if we're in development mode. If so, we don't
        // add the normal web-app location, but the web-app in the project directory.
        context = new WebAppContext(contexts, webroot,
                "/");

        context.setWelcomeFiles(new String[]{"index.jsp"});

        return context;

    }


    /**
     * Starts an HTTP Bind server on the specified port and secure port.
     *
     * @param port the port to start the normal (unsecured) HTTP Bind service on.
     */
    private synchronized void configureHttpBindServer(int port) {
        // this is the number of threads allocated to each connector/port
        int bindThreads = 8;

        final QueuedThreadPool tp = new QueuedThreadPool();
        tp.setName("Jetty-QTP-HttpServer");

        httpBindServer = new Server(tp);
        createConnector(port, bindThreads);
        if (httpConnector == null) {
            httpBindServer = null;
            return;
        }
        if (httpConnector != null) {
            httpBindServer.addConnector(httpConnector);
        }

        //contexts = new ContextHandlerCollection();
        // TODO implement a way to get plugins to add their their web services to contexts

        createCrossDomainHandler(contexts, "/crossdomain.xml");

        // Make sure that at least one connector was registered.
        if (httpBindServer.getConnectors() == null || httpBindServer.getConnectors().length == 0) {
            httpBindServer = null;
            // Log warning.
            Log.info("初始化httpServer的connector失败");
            return;
        }

        HandlerCollection collection = new HandlerCollection();
        httpBindServer.setHandler(collection);
        collection.setHandlers(new Handler[]{contexts, new DefaultHandler()});
    }

    private void createConnector(int port, int bindThreads) {
        httpConnector = null;
        if (port > 0) {
            HttpConfiguration httpConfig = new HttpConfiguration();
            // Do not send Jetty info in HTTP headers
            httpConfig.setSendServerVersion(false);
            ServerConnector connector = new ServerConnector(httpBindServer, null, null, null, -1, bindThreads,
                    new HttpConnectionFactory(httpConfig));

            // Listen on a specific network interface if it has been set.
            connector.setHost(getBindInterface());
            connector.setPort(port);
            httpConnector = connector;
        }
    }

    private String getBindInterface() {
        String interfaceName = JeeFrameWorkServer.getInstance().getServerProperties().getProperty(HTTP_BIND_HOST, HTTP_BIND_HOST_DEFAULT);
        String bindInterface = null;
        if (interfaceName != null) {
            if (interfaceName.trim().length() > 0) {
                bindInterface = interfaceName;
            }
        }
        return bindInterface;
    }

    private void createCrossDomainHandler(ContextHandlerCollection contexts, String crossPath) {
        ServletContextHandler context = new ServletContextHandler(contexts, crossPath, ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new FlashCrossDomainServlet()), "");
    }


    public static void main(String[] args) throws IOException, XmlPullParserException {


//        File[] commonsLang = Maven.resolver().loadPomFromFile("F:\\myproject\\intellij\\testgitlite\\jeeframework\\" + "pom.xml").importDependencies(ScopeType.TEST, ScopeType.PROVIDED, ScopeType.COMPILE, ScopeType.RUNTIME).resolve().withTransitivity().asFile();
//
//        for (File file : commonsLang) {
//            try {
//                System.out.println(file.getCanonicalPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        File pomFile = new File("F:\\myproject\\intellij\\testgitlite\\jeeframework\\" + "pom.xml");
//
//        MavenProject proj = loadProject(pomFile);
//
//        List<Dependency> dependencies = proj.getDependencies();
//        Iterator<Dependency> it = dependencies.iterator();
//
//        while (it.hasNext()) {
//            org.apache.maven.model.Dependency depend = it.next();
//
//            System.out.println(depend);
//
////             depend.getGroupId(), depend.getArtifactId(), depend.getClassifier(), depend.getType(), depend.getVersion()   ;
//
//        }
    }

    public static MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException {
        MavenProject ret = null;
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();

        if (pomFile != null && pomFile.exists()) {
            FileReader reader = null;

            try {
                reader = new FileReader(pomFile);
                Model model = mavenReader.read(reader);
                model.setPomFile(pomFile);

                ret = new MavenProject(model);
            } finally {
                reader.close();
            }
        }

        return ret;
    }
}
