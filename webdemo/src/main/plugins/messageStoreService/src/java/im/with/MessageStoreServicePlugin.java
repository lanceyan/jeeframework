/*
 * @project: Openfire 
 * @package: im.with
 * @title:   MessageStoreServicePlugin.java
 *
 * Copyright (c) 2015 with Limited, Inc.
 * All rights reserved.
 */
package im.with;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;

import java.io.File;

/**
 * 用于存储用户的消息，并发送消息确认防止消息丢失
 *
 * @author lance
 * @version 1.0 2015-09-25 16:56
 */
public class MessageStoreServicePlugin implements Plugin {

    private MessageStoreServiceInterceptor messageStoreServiceInterceptor = null;

    public void destroyPlugin() {
        if (messageStoreServiceInterceptor != null) {
            InterceptorManager.getInstance().removeInterceptor(messageStoreServiceInterceptor);
        }
    }

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        messageStoreServiceInterceptor = new MessageStoreServiceInterceptor();
        InterceptorManager.getInstance().addInterceptor(messageStoreServiceInterceptor);
    }
}
