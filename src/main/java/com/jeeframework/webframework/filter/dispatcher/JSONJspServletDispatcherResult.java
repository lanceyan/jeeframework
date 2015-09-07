/*
 * $Id: JSONJspServletDispatcherResult.java 471756 2006-11-06 15:01:43Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jeeframework.webframework.filter.dispatcher;

import com.jeeframework.util.io.FileUtils;
import com.jeeframework.util.string.StringUtils;
import com.jeeframework.webframework.filter.StrutsPrepareAndExecuteFilterWrapper;
import com.opensymphony.xwork2.ActionInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.File;
import java.io.IOException;

/**
 * <!-- START SNIPPET: description -->
 * 
 * Includes or forwards to a view (usually a jsp). Behind the scenes Struts will
 * use a RequestDispatcher, where the target servlet/JSP receives the same
 * request/response objects as the original servlet/JSP. Therefore, you can pass
 * data between them using request.setAttribute() - the Struts action is
 * available. <p/> There are three possible ways the result can be executed:
 * 
 * <ul>
 * 
 * <li>If we are in the scope of a JSP (a PageContext is available),
 * PageContext's {@link PageContext#include(String) include} method is called.</li>
 * 
 * <li>If there is no PageContext and we're not in any sort of include (there
 * is no "javax.servlet.include.servlet_path" in the request attributes), then a
 * call to
 * {@link RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse) forward}
 * is made.</li>
 * 
 * <li>Otherwise,
 * {@link RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse) include}
 * is called.</li>
 * 
 * </ul>
 * <!-- END SNIPPET: description -->
 * 
 * <b>This result type takes the following parameters:</b>
 * 
 * <!-- START SNIPPET: params -->
 * 
 * <ul>
 * 
 * <li><b>location (default)</b> - the location to go to after execution (ex.
 * jsp).</li>
 * 
 * <li><b>parse</b> - true by default. If set to false, the location param
 * will not be parsed for Ognl expressions.</li>
 * 
 * </ul>
 * 
 * <!-- END SNIPPET: params -->
 * 
 * <b>Example:</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;result name=&quot;success&quot; type=&quot;dispatcher&quot;&gt;
 *   &lt;param name=&quot;location&quot;&gt;foo.jsp&lt;/param&gt;
 * &lt;/result&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 * 
 * This result follows the same rules from {@link StrutsResultSupport}.
 * 
 * @see RequestDispatcher
 */
public class JSONJspServletDispatcherResult extends ServletDispatcherResult {

	private static final long serialVersionUID = -1970659272360685627L;

	private static final Log log = LogFactory.getLog(JSONJspServletDispatcherResult.class);

	public static final String JSONJSP_SUFFIX = "_JSONJsp";
	public static final String JSONJSP_PREFIX = "jsonjsp";

	public JSONJspServletDispatcherResult() {
		super();
	}

	public JSONJspServletDispatcherResult(String location) {
		super(location);
	}

	/**
	 * Dispatches to the given location. Does its forward via a
	 * RequestDispatcher. If the dispatch fails a 404 error will be sent back in
	 * the http response.
	 * 
	 * @param finalLocation
	 *            the location to dispatch to.
	 * @param invocation
	 *            the execution state of the action
	 * @throws Exception
	 *             if an error occurs. If the dispatch fails the error will go
	 *             back via the HTTP request.
	 */
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Forwarding to location " + finalLocation);
		}

		PageContext pageContext = ServletActionContext.getPageContext();

		if (pageContext != null) {
			pageContext.include(finalLocation);
		} else {

		    HttpServletRequest request = ServletActionContext.getRequest();
                    HttpServletResponse response = ServletActionContext.getResponse();

                    String curFileName = StringUtils.getFileNamePreffix(finalLocation);
                    String curFileSuffix = StringUtils.getFilenameExtension(finalLocation);
                    String dstFile = curFileName;

                    if (curFileName.startsWith(JSONJspServletDispatcherResult.JSONJSP_PREFIX)
                                    && curFileName.length() > JSONJspServletDispatcherResult.JSONJSP_PREFIX.length()) {
                            dstFile = curFileName.substring(JSONJspServletDispatcherResult.JSONJSP_PREFIX.length());
                    }
                    if (curFileName.endsWith(JSONJspServletDispatcherResult.JSONJSP_SUFFIX)
                                    && curFileName.length() > JSONJspServletDispatcherResult.JSONJSP_SUFFIX.length()) {
                            dstFile = dstFile.substring(0, dstFile.length() - JSONJspServletDispatcherResult.JSONJSP_SUFFIX.length());
                    }

                    String webroot = System.getProperty(StrutsPrepareAndExecuteFilterWrapper.CUR_DEFAULT_WEBROOT_KEY);
                    if (!webroot.endsWith("/") && !webroot.endsWith("\\")) {
                            webroot = webroot + "/";
                    }
                    

                    String dstFilePath = webroot + finalLocation;

                    File dstCurjsp = new File(dstFilePath);
                    // && !dstCurjsp.exists()
                    if (!dstCurjsp.exists()) {
                            try {
                                    String srcFilePath = webroot;//
                                    if (!(dstFile.startsWith("/") || dstFile.startsWith("\\"))) {
                                            srcFilePath = srcFilePath + "/" ;
                                    }
                                    srcFilePath = srcFilePath + dstFile + "." + curFileSuffix;
                                    
                                    File srcCurjsp = new File(srcFilePath);
                                    
                                    String srcCurjspContent = FileUtils.readFileToString(srcCurjsp, StrutsPrepareAndExecuteFilterWrapper.FILE_ENCODING);
                                    srcCurjspContent = srcCurjspContent.replaceAll("[\r\n\t]", "");

                                    FileUtils.writeStringToFile(dstCurjsp, srcCurjspContent, StrutsPrepareAndExecuteFilterWrapper.FILE_ENCODING);
				
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("生成 jsonjsp " + dstFilePath + "error出错");
				}
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);

			// if the view doesn't exist, let's do a 404
			if (dispatcher == null) {
				response.sendError(404, "result '" + finalLocation + "' not found");

				return;
			}

			// If we're included, then include the view
			// Otherwise do forward
			// This allow the page to, for example, set content type
			if (!response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
				request.setAttribute("struts.view_uri", finalLocation);
				request.setAttribute("struts.request_uri", request.getRequestURI());

				dispatcher.forward(request, response);
			} else {
				dispatcher.include(request, response);
			}
		}
	}
}
