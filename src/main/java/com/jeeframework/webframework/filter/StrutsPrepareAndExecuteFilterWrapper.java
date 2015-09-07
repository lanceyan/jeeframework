package com.jeeframework.webframework.filter;

import com.jeeframework.util.validate.Validate;
import com.jeeframework.webframework.filter.util.InitOperationsWrapper;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.ng.ExecuteOperations;
import org.apache.struts2.dispatcher.ng.PrepareOperations;
import org.apache.struts2.dispatcher.ng.filter.FilterHostConfig;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StrutsPrepareAndExecuteFilterWrapper extends StrutsPrepareAndExecuteFilter {
	public static String CUR_DEFAULT_WEBROOT_KEY = "webapp.root";
	public static String FILE_ENCODING = "UTF-8";
	
    public void init(FilterConfig filterConfig) throws ServletException {
        InitOperationsWrapper init = new InitOperationsWrapper();
        Dispatcher dispatcher = null;
        try {
        	
        	String local_CUR_DEFAULT_WEBROOT_KEY = filterConfig.getServletContext().getInitParameter("webAppRootKey");

    		if (!Validate.isEmpty(local_CUR_DEFAULT_WEBROOT_KEY)) {
    			CUR_DEFAULT_WEBROOT_KEY = local_CUR_DEFAULT_WEBROOT_KEY;
    		}
    		
    		String local_FILE_ENCODING = filterConfig.getServletContext().getInitParameter("encoding");

    		if (!Validate.isEmpty(local_FILE_ENCODING)) {
    			FILE_ENCODING = local_FILE_ENCODING;
    		}
    		
    		
            FilterHostConfig config = new FilterHostConfig(filterConfig);
            init.initLogging(config);
            dispatcher = init.initDispatcher(config);
            init.initStaticContentLoader(config, dispatcher);

            prepare = new PrepareOperations(filterConfig.getServletContext(), dispatcher);
            execute = new ExecuteOperations(filterConfig.getServletContext(), dispatcher);
            this.excludedPatterns = init.buildExcludedPatternsList(dispatcher);

            postInit(dispatcher, filterConfig);
        } finally {
            if (dispatcher != null) {
                dispatcher.cleanUpAfterInit();
            }
            init.cleanup();
        }
    }
    
    private boolean isRestUrl(String url) {

		if (url.length() > 1 && url.startsWith("/")) {
			url = url.substring(1);
		}

		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if (url.indexOf(".") != -1 || url.length() <= 0) {
			return false;
		}
		return true;
	}
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            prepare.setEncodingAndLocale(request, response);
            prepare.createActionContext(request, response);
            prepare.assignDispatcherToThread();
            if (excludedPatterns != null && prepare.isUrlExcluded(request, excludedPatterns)) {
                chain.doFilter(request, response);
            } else {
                request = prepare.wrapRequest(request);
                ActionMapping mapping = prepare.findActionMapping(request, response, true);
                if (mapping == null) {
                    boolean handled = execute.executeStaticResourceRequest(request, response);
                    if (!handled) {
                        chain.doFilter(request, response);
                    }
                } else {
                    execute.executeAction(request, response, mapping);
                }
            }
        } finally {
            prepare.cleanupRequest(request);
        }
    }
}
