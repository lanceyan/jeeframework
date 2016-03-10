package com.jeeframework.logicframework.util.server;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.util.validate.Validate;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.annotation.Name;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;

@SuppressWarnings("unchecked")
public class JettyServer extends Server implements ApplicationContextAware {

	/*
	 * To embed a Jetty server, the following steps are typical: 1. Create the
	 * server 2. Add/Configure Connectors 3. Add/Configure Handlers 4.
	 * Add/Configure Servlets/Webapps to Handlers 5. start the server 6. wait
	 * (join the server to prevent main exiting).
	 */

	private ApplicationContext applicationContext;

	public JettyServer(@Name("threadpool") ThreadPool pool) {
		super(pool);
	}

	public void startServer() throws Exception {

		HandlerCollection collection = new HandlerCollection();

		Handler[] handlers = this.getHandlers();

		// RequestLogHandler logHandler = new RequestLogHandler();
		// NCSARequestLog log = new NCSARequestLog();
		// log.setFilename("target/request.log");
		// // log.setAppend(true);
		// logHandler.setRequestLog(log);

		WebAppContext webAppContext = new WebAppContext();

		// ServletContext servletContext = webAppContext.getServletContext();
		// String realPath = servletContext.getRealPath("");

		String userDir = System.getProperty("user.dir");

		String webrootHome = null;

		webrootHome = userDir + "/src/main/" + "webapp/";
		if (!new File(webrootHome).exists()) {
			webrootHome = null;
		}
		if (webrootHome == null) {
			webrootHome = System.getProperty("webroot");
			if (!new File(webrootHome).exists()) {
				throw new BizException("webrootHome 不存在 ：" + webrootHome);
			}
		}

		webAppContext.setContextPath("/");
		webAppContext.setDefaultsDescriptor(webrootHome
				+ "WEB-INF/webdefault.xml");
		webAppContext.setDescriptor(webrootHome + "WEB-INF/webjetty.xml");
		webAppContext.setResourceBase(webrootHome);
		webAppContext.setConfigurationDiscovered(true);
		webAppContext.setParentLoaderPriority(true);
		// 设置为form可以提交无限的文件
		webAppContext.setMaxFormContentSize(-1);

		collection.addHandler(webAppContext);

		if (!Validate.isEmpty(handlers)) {
			for (Handler handler : handlers) {
				collection.addHandler(handler);
			}
		}
		this.setHandler(collection);

		// 以下代码是关键
		webAppContext.setClassLoader(applicationContext.getClassLoader());

		XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
		xmlWebAppContext.setParent(applicationContext);
		xmlWebAppContext.setConfigLocation("");
		xmlWebAppContext.setServletContext(webAppContext.getServletContext());
		xmlWebAppContext.refresh();

		webAppContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				xmlWebAppContext);

		// PrintStream ServerLog =

		// System.setErr(err)

		// 开启debug模式，信息将打印控制台上，否则到log中
		boolean jettyDebug = false;
		try {
			jettyDebug = Boolean.valueOf(System.getProperty("jettyDebug"));
		} catch (Exception e) {
			jettyDebug = false;
		}

		if (!jettyDebug) {
			String log_dir = System.getProperty("log.dir");

			java.io.PrintStream serverLog = new java.io.PrintStream(
					new org.eclipse.jetty.util.RolloverFileOutputStream(log_dir
							+ "/yyyy_mm_dd.jettyStdouterr.log", true, 30));

			java.lang.System.setErr(serverLog);
			java.lang.System.setOut(serverLog);
		}

		this.start();
		ServletContext servletContext = webAppContext.getServletContext();
		String realPath = servletContext.getRealPath("");

		System.out.println("启动路径在 realPath: " + realPath);

	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;

	}
}