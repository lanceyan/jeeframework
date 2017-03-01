/*
 * @project: webdemo 
 * @package: com.jeeframework.logicframework.util.server.tcp
 * @title:   MinaTcpServer.java 
 *
 * Copyright (c) 2016 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.util.server.tcp;

import com.jeeframework.logicframework.util.server.JeeFrameWorkServer;
import com.jeeframework.logicframework.util.server.PathMatchingResourcePatternResolverWrapper;
import com.jeeframework.logicframework.util.server.tcp.codec.MinaServerCodecFactory;
import com.jeeframework.logicframework.util.server.tcp.protocol.ProtocolParser;
import com.jeeframework.logicframework.util.server.tcp.worker.ClientConnectionHandler;
import com.jeeframework.util.properties.JeeProperties;
import com.jeeframework.util.resource.ResolverUtil;
import com.jeeframework.util.string.StringUtils;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jeeframework.logicframework.util.server.JeeFrameWorkServer.NET_CONTROLLER_PACKAGES;

/**
 * mina网络协议接口
 *
 * @author lance
 * @version 1.0 2016-03-08 23:38
 */
public class MinaTcpServer {

    private static final Logger Log = LoggerFactory.getLogger(MinaTcpServer.class);
    private static final String CLIENT_SOCKET_ACCEPTOR_NAME = "client";

    private static final int MB = 1024 * 1024;

    public static final String SOCKET_BIND_PORT = "socket.port";
    public static final int SOCKET_BIND_PORT_DEFAULT = 5050;


    public static final String CONNECTION_MAX_THREADS = "conn.maxthreads";
    public static final int CONNECTION_MAX_THREADS_DEFAULT = 16;

    public static final String CONNECTION_MAX_READ_BUFFER = "conn.maxreadbuffer";
    public static final int CONNECTION_MAX_READ_BUFFER_DEFAULT = 10 * MB;

    public static final String EXECUTOR_FILTER_NAME = "threadModel";

    public static final String SOCKET_BIND_HOST = "socket.host";
    public static final String SOCKET_BIND_HOST_DEFAULT = "0.0.0.0";

    public static final String SOCKET_PROCESSOR_COUNT = "processor.count";
    public static final String SOCKET_BACKLOG = "socket.backlog";

    public static final String SOCKET_BUFFER_RECEIVE = "socket.buffer.receive";
    public static final String SOCKET_BUFFER_SEND = "socket.buffer.send";
    public static final String SOCKET_LINGER = "socket.linger";
    public static final String SOCKET_TCP_NODELAY = "socket.tcp-nodelay";

    public static final String CONNECTION_WORKER_COUNT = "conn.worker.count";

    public static final int SOCKET_PROCESSOR_COUNT_DEFAULT = Runtime.getRuntime().availableProcessors();
    public static final int SOCKET_BACKLOG_DEFAULT = 50;

    public static final int SOCKET_BUFFER_RECEIVE_DEFAULT = -1;
    public static final int SOCKET_BUFFER_SEND_DEFAULT = -1;
    public static final int SOCKET_LINGER_DEFAULT = -1;
    public static final int CONNECTION_WORKER_COUNT_DEFAULT = 3;

    private NioSocketAcceptor socketAcceptor;
    private JeeProperties serverProperties;//JeeFrameWorkServer.getInstance().getServerProperties();

    private String localIPAddress = null;


    private ApplicationContext applicationContext;

    public MinaTcpServer(ApplicationContext applicationContext, JeeProperties serverProperties) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
    }

    public void start() {

//        SocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
//        //设置解析器
//        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
//        chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
//
//        acceptor.setHandler(new SimpleMinaMessageHandler());
//        try {
//            acceptor.bind(new InetSocketAddress(5050));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        // Create SocketAcceptor with correct number of processors
        socketAcceptor = buildSocketAcceptor(CLIENT_SOCKET_ACCEPTOR_NAME);

        int maxPoolSize = serverProperties.getIntProperty(CONNECTION_MAX_THREADS, CONNECTION_MAX_THREADS_DEFAULT);
        ExecutorFilter executorFilter = new ExecutorFilter(getCorePoolSize(maxPoolSize), maxPoolSize, 60, TimeUnit.SECONDS);
        ThreadPoolExecutor eventExecutor = (ThreadPoolExecutor) executorFilter.getExecutor();
        ThreadFactory threadFactory = eventExecutor.getThreadFactory();
        threadFactory = new DelegatingThreadFactory("Socket-Thread-", threadFactory);
        eventExecutor.setThreadFactory(threadFactory);

        // Add the XMPP codec filter
        socketAcceptor.getFilterChain().addFirst(EXECUTOR_FILTER_NAME, executorFilter);
        socketAcceptor.getFilterChain().addAfter(EXECUTOR_FILTER_NAME, "codec", new ProtocolCodecFilter(new MinaServerCodecFactory()));
//        socketAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaServerCodecFactory()));
        // Kill sessions whose outgoing queues keep growing and fail to send traffic
//        socketAcceptor.getFilterChain().addAfter(XMPP_CODEC_FILTER_NAME, CAPACITY_FILTER_NAME, new StalledSessionsFilter());


        // Throttle sessions who send data too fast
        int maxBufferSize = serverProperties.getIntProperty(CONNECTION_MAX_READ_BUFFER, CONNECTION_MAX_READ_BUFFER_DEFAULT);
        socketAcceptor.getSessionConfig().setMaxReadBufferSize(maxBufferSize);
        Log.debug("Throttling read buffer for connections from socketAcceptor={} to max={} bytes",
                socketAcceptor, maxBufferSize);

        // Setup port info
        try {
            localIPAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            if (localIPAddress == null) {
                localIPAddress = "Unknown";
            }
        }

        // Start the port listener for clients
        startClientListeners(localIPAddress);

        if (applicationContext != null) {
            try {
                scanAndRegistActionClass(applicationContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从配置文件获得netserver的配置。
     * 扫描配置的路径，并把继承NetBusiness类的Action注入容器。
     *
     * @param context 容器池
     * @throws Exception
     */
    public void scanAndRegistActionClass(ApplicationContext context) throws Exception {

        String packages = serverProperties.getProperty(NET_CONTROLLER_PACKAGES);
        ;
        Set<Class> actionClassesSet = new HashSet<Class>(); // 装载
        // action的class
        // 集合
        // 扫描需要找的的action类
        if (packages != null) {
            // String[] names = packages.split("\\s*[,]\\s*");
            // lanceyan 增加解析各个包路径，包括通配符
            String[] names = StringUtils.tokenizeToStringArray(packages, ",; \t\n", true, true);
            // Initialize the classloader scanner with the configured
            // packages
            List<String> allPackagePath = new ArrayList<String>();
            if (names.length > 0) {
                for (String name : names) {
                    name = name.replace('.', '/');
                    String[] resourceNames = null;
                    PathMatchingResourcePatternResolverWrapper resourceLoader = new PathMatchingResourcePatternResolverWrapper();
                    try {
                        // 获取根路径  modify bylanceyan  ，增加获取jar包和classpath路径的方法
                        Resource[] actionResources = ((ResourcePatternResolver) resourceLoader)
                                .getResources("classpath*:" + name);

                        if (actionResources != null) {
                            resourceNames = new String[actionResources.length];
                            for (int i = 0; i < actionResources.length; i++) {
                                // 得到相对路径，然后替换为包的命名方式
                                resourceNames[i] = resourceLoader.getRelativeResourcesPath(actionResources[i]);

                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Loaded action configuration from:getAllResource()");
                    }

//                    String[] resourceNames = PathMatchingResourcePatternResolverWrapper.getAllResource(name);

                    if (resourceNames != null && resourceNames.length > 0) {
                        for (String resourceName : resourceNames) {
                            allPackagePath.add(resourceName);
                        }
                    }
                }
                loadActionClass(actionClassesSet, allPackagePath.toArray(new String[0]));
            }
        }

        // 注册扫描到的action
        for (Object obj : actionClassesSet) {
            Class cls = (Class) obj;
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
            RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClass(cls);
            def.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);

            String beanName = StringUtils.uncapitalize(cls.getSimpleName());

            System.out.println("Net action class  beanName   " + beanName + " has  loaded in context! ");
            beanFactory.registerBeanDefinition(beanName, def);


        }

    }


    /**
     * 从扫描得到的action集合把继承NetBusiness的类加载到容器池。
     *
     * @param actionClassesSet action class集合
     * @param pkgs             容器池
     */
    protected void loadActionClass(Set<Class> actionClassesSet, String[] pkgs) {
        ResolverUtil<Class> resolver = new ResolverUtil<Class>();
        resolver.find(new ResolverUtil.Test() {
            // 回调函数，用于校验类是否是继承NetBusiness
            public boolean matches(Class type) {
                // TODO: should also find annotated classes
                return (BaseNetController.class.isAssignableFrom(type));
            }

        }, pkgs);

        Set<? extends Class<? extends Class>> actionClasses = resolver.getClasses();
        for (Object obj : actionClasses) {
            Class cls = (Class) obj;
            if (!Modifier.isAbstract(cls.getModifiers())) {
                // ClassPathXmlApplicationContext context1;
                // context1.

                actionClassesSet.add(cls);
                ProtocolParser.registerNetControllerClazz(cls); // 在服务器里注册加了annotation的方法
                // 比如： @Protocol(cmdId = 0x26211803, desc = "根据角色获取留言", export =
                // true)
            }
        }
    }


    public int getClientListenerPort() {
        return serverProperties.getIntProperty(SOCKET_BIND_PORT, SOCKET_BIND_PORT_DEFAULT);
    }

    private void startClientListeners(String localIPAddress) {
        // Start clients plain socket unless it's been disabled.
        int port = getClientListenerPort();
        try {
            // Listen on a specific network interface if it has been set.
            String interfaceName = serverProperties.getProperty(SOCKET_BIND_HOST, SOCKET_BIND_HOST_DEFAULT);
            InetAddress bindInterface = null;
            if (interfaceName != null) {
                if (interfaceName.trim().length() > 0) {
                    bindInterface = InetAddress.getByName(interfaceName);
                }
            } else {
                bindInterface = InetAddress.getByName(localIPAddress);
            }

            int workerCount = serverProperties.getIntProperty(CONNECTION_WORKER_COUNT, CONNECTION_WORKER_COUNT_DEFAULT);

            // Start accepting connections
            socketAcceptor.setHandler(new ClientConnectionHandler(this, workerCount));

            socketAcceptor.bind(new InetSocketAddress(bindInterface, port));


            List<String> params = new ArrayList<String>();
            params.add(Integer.toString(port));
            Log.info("start socket server in  address = " + interfaceName + " , port =  " + port);
        } catch (Exception e) {
            System.err.println("Error starting socket listener on port " + port + ": " +
                    e.getMessage());
            Log.error("error.socket-setup", e);
        }
    }

    private void stopClientListeners() {
        if (socketAcceptor != null) {
            socketAcceptor.unbind();
            socketAcceptor = null;
        }
    }

    private int getCorePoolSize(int maxPoolSize) {
        return (maxPoolSize / 4) + 1;
    }

    private NioSocketAcceptor buildSocketAcceptor(String name) {
        NioSocketAcceptor socketAcceptor;
        // Create SocketAcceptor with correct number of processors
        int processorCount = serverProperties.getIntProperty(SOCKET_PROCESSOR_COUNT, SOCKET_PROCESSOR_COUNT_DEFAULT);
        socketAcceptor = new NioSocketAcceptor(processorCount);
        // Set that it will be possible to bind a socket if there is a connection in the timeout state
        socketAcceptor.setReuseAddress(true);
        // Set the listen backlog (queue) length. Default is 50.
        socketAcceptor.setBacklog(serverProperties.getIntProperty(SOCKET_BACKLOG, SOCKET_BACKLOG_DEFAULT));

        // Set default (low level) settings for new socket connections
        SocketSessionConfig socketSessionConfig = socketAcceptor.getSessionConfig();
        //socketSessionConfig.setKeepAlive();
        int receiveBuffer = serverProperties.getIntProperty(SOCKET_BUFFER_RECEIVE, SOCKET_BUFFER_RECEIVE_DEFAULT);
        if (receiveBuffer > 0) {
            socketSessionConfig.setReceiveBufferSize(receiveBuffer);
        }
        int sendBuffer = serverProperties.getIntProperty(SOCKET_BUFFER_SEND, SOCKET_BUFFER_SEND_DEFAULT);
        if (sendBuffer > 0) {
            socketSessionConfig.setSendBufferSize(sendBuffer);
        }
        int linger = serverProperties.getIntProperty(SOCKET_LINGER, SOCKET_LINGER_DEFAULT);
        if (linger > 0) {
            socketSessionConfig.setSoLinger(linger);
        }
        socketSessionConfig.setTcpNoDelay(
                serverProperties.getBooleanProperty(SOCKET_TCP_NODELAY, socketSessionConfig.isTcpNoDelay()));

        return socketAcceptor;
    }

    private static class DelegatingThreadFactory implements ThreadFactory {
        private final AtomicInteger threadId;
        private final ThreadFactory originalThreadFactory;
        private String threadNamePrefix;

        public DelegatingThreadFactory(String threadNamePrefix, ThreadFactory originalThreadFactory) {
            this.originalThreadFactory = originalThreadFactory;
            threadId = new AtomicInteger(0);
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            Thread t = originalThreadFactory.newThread(runnable);
            t.setName(threadNamePrefix + threadId.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    public static void main(String[] args) {
        JeeProperties serverProperties = new JeeProperties(JeeFrameWorkServer.SERVER_CONFIG_FILE, false);
        MinaTcpServer minaTcpServer = new MinaTcpServer(null, serverProperties);
        minaTcpServer.start();
    }

}
