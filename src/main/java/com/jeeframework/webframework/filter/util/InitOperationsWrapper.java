package com.jeeframework.webframework.filter.util;

import com.jeeframework.webframework.filter.dispatcher.DispatcherWrapper;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.HostConfig;
import org.apache.struts2.dispatcher.ng.InitOperations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InitOperationsWrapper extends InitOperations {
	
	/**
     * Creates and initializes the dispatcher
     */
    public Dispatcher initDispatcher( HostConfig filterConfig ) {
        Dispatcher dispatcher = createDispatcher(filterConfig);
        dispatcher.init();
        return dispatcher;
    }
    

    /**
     * Create a {@link Dispatcher}
     */
    private Dispatcher createDispatcher( HostConfig filterConfig ) {
        Map<String, String> params = new HashMap<String, String>();
        for ( Iterator e = filterConfig.getInitParameterNames(); e.hasNext(); ) {
            String name = (String) e.next();
            String value = filterConfig.getInitParameter(name);
            params.put(name, value);
        }
        return new DispatcherWrapper(filterConfig.getServletContext(), params);
    }

}
