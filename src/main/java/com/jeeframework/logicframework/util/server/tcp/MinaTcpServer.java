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
import com.jeeframework.logicframework.util.server.tcp.codec.MinaServerCodecFactory;
import com.jeeframework.logicframework.util.server.tcp.worker.ClientConnectionHandler;
import com.jeeframework.util.properties.JeeProperties;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    private JeeProperties serverProperties = JeeFrameWorkServer.getInstance().getServerProperties();

    private String localIPAddress = null;

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
            }
            else {
                bindInterface = InetAddress.getByName(localIPAddress);
            }

            int workerCount = serverProperties.getIntProperty(CONNECTION_WORKER_COUNT, CONNECTION_WORKER_COUNT_DEFAULT);

            // Start accepting connections
            socketAcceptor.setHandler(new ClientConnectionHandler(workerCount));

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

    public static void main(String[] args) {
        MinaTcpServer minaTcpServer = new MinaTcpServer();
        minaTcpServer.start();
    }

}
