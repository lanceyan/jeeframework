package com.jeeframework.webframework.listener;

import com.jeeframework.core.context.support.AfterLoadContextEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * 有些bean不是在一开始就需要加载，需要等待一些bean加载完了后在加载，由此需要实现一个afterLoadContextEvent
 */
public class ContextLoaderListenerWrapper extends ContextLoaderListener {

	private ContextLoader contextLoader;

	/**
	 * Initialize the root web application context.
	 */
	public void contextInitialized(ServletContextEvent event) {
		this.contextLoader = createContextLoader();
		ApplicationContext xac = this.contextLoader.initWebApplicationContext(event.getServletContext());

		System.out.println(  "*********准备初始化spring后置处理器 *********");
		AfterLoadContextEvent afterLoadContextEvent = new AfterLoadContextEvent("afterLoadContext");
		xac.publishEvent(afterLoadContextEvent);
	}

	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * 
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	/**
	 * Return the ContextLoader used by this listener.
	 * 
	 * @return the current ContextLoader
	 */
	public ContextLoader getContextLoader() {
		return this.contextLoader;
	}

	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(event.getServletContext());
		}
	}
}
