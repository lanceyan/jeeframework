package com.jeeframework.logicframework.common.remote;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.jeeframework.core.context.Daemon;
import com.jeeframework.core.context.support.AfterLoadContextEvent;
import com.jeeframework.core.context.support.SpringContextLoader;
import com.jeeframework.util.validate.Validate;

public class RemoteServerDaemon extends Daemon implements Runnable {

	private String[] args;

	/**
	 * 初始化类的方法，包括初始化本地server端口监听
	 * 
	 * @param 参数说明：每个参数一行，注明其取值范围等
	 * @return 返回值：注释出失败、错误、异常时的返回情况
	 * @exception 异常：注释出什么条件下会引发什么样的异常
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */

	public RemoteServerDaemon(String[] args) {
		this.args = args;
	}

	public void run() {
		String transferType = "";

		if (this.args.length != 1) {
			usage("Wrong number of arguments.");
		}

		PropertyReaderUtil propUtil = new PropertyReaderUtil();
		PropertyReaderUtil bizPropUtil = new PropertyReaderUtil();

		try {

			org.springframework.core.io.ClassPathResource daemonResource = new org.springframework.core.io.ClassPathResource(args[0]);
			propUtil.setLocation(daemonResource);
			propUtil.afterPropertiesSet();

			String configFile = propUtil.getParseValue("contextConfigLocation");

			String bizProp = propUtil.getParseValue("biz.properties");

			if (Validate.isEmpty(bizProp)) {
				usage(" RemoteServerDaemon biz.properties set error or null. ");
			}

			org.springframework.core.io.ClassPathResource bizResource = new org.springframework.core.io.ClassPathResource(bizProp);
			bizPropUtil.setLocation(bizResource);
			bizPropUtil.afterPropertiesSet();

			String curRransferType = bizPropUtil.getParseValue("server.transfer.type");
			if (!Validate.isEmpty(curRransferType)) {
				transferType = curRransferType.trim();
			}

			if (configFile == null || configFile.trim().length() <= 0) {
				usage(" RemoteServerDaemon configfile set error or null. ");
			}
			
			AbstractApplicationContext  aac = SpringContextLoader.loadContext(configFile);

			try {
				if (!Validate.isEmpty(transferType)) {
					if (transferType.equals("jetty")) {

						JettyServer jettyServer = (JettyServer)aac.getBean("jettyServer");
						
						if(jettyServer!=null)
						{
							jettyServer.startServer();
						}
						
						
						System.out.println(  "*********容器全部加载完成，执行后续处理**********");
						AfterLoadContextEvent afterLoadContextEvent = new AfterLoadContextEvent("afterLoadContext");
						// 在ApplicationContext中发布一个 ApplicationEvent
						aac.publishEvent(afterLoadContextEvent);
						
//						Server server = new Server();
//
//						ClassPathResource resource = new ClassPathResource("jetty.xml");
//
//						if (!resource.exists())// 检查是否存在
//						{
//							throw new RuntimeException("jetty.xml资源文件不存在!");
//						}
//						XmlConfiguration configuration;
//
//						configuration = new XmlConfiguration(resource.getInputStream());
//
//						configuration.configure(server);
//						
//						server.start();

//						Handler[] handerArray = server.getHandlers();
//
//						for (Handler handler : handerArray) {
//
//							if (handler instanceof HandlerList) {
//								HandlerList handlerList = (HandlerList) handler;
//								Handler[] handerArray1 = handlerList.getHandlers();
//								
//								callAfterInitContext(handerArray1);
//							} else if(handler instanceof HandlerCollection) {
//								HandlerCollection handlerList = (HandlerCollection) handler;
//								Handler[] handerArray1 = handlerList.getHandlers();
//								
//								callAfterInitContext(handerArray1);
//							}
//
//						}

						System.out.println("*******后台服务成功启动！*******");

						return;// 不继续往下一步执行
					}

					// 没有填直接初始化
				}

			} 
//			catch (SAXException e) {
//				throw e;
//			} catch (IOException e) {
//				throw e;
//			} 
			catch (Exception e) {
				e.printStackTrace();
				throw e;
			} // 指定自定义的jetty.xml路径

//			if (Validate.isEmpty(configFile)) {
//				usage(" RemoteServerDaemon contextConfigLocation set error or null. ");
//			}


		} catch (IOException e) {
			e.printStackTrace();
			usage("read configfile error .   "  );
			
		} catch (Exception e) {
			e.printStackTrace();
			usage("read configfile error .   "  );
			
			
		}
	}

	private void callAfterInitContext(Handler[] handerArray1) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		for (Handler handler1 : handerArray1) {
			if (handler1 instanceof ContextHandler) {

				ContextHandler contextHandler = (ContextHandler) handler1;

				ServletContext servletContext = contextHandler.getServletContext();

				//因为appcontext的classloader和当前这个classloader不一样，需要转换一下才能调用其中的方法
				Object appContext = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
				ClassLoader contextClassLoader = appContext.getClass().getClassLoader();
				
				Thread.currentThread().setContextClassLoader(contextClassLoader);

				Class afterSpringContext = contextClassLoader.loadClass("com.jeeframework.core.context.support.AfterSpringContext");

				// Class cc =
				// Class.forName("com.jeeframework.logicframework.common.remote.AfterSpringContext");
				Object afterSpringContextObj = afterSpringContext.newInstance();

				// Method method =
				// cc.getDeclaredMethod("doAfter1" );
				//										
				// Object obj1 = method.invoke(cobj);

				Method method = afterSpringContext.getDeclaredMethod("doAfter", Object.class);
				method.invoke(afterSpringContextObj, appContext);

			}
		}
	}

	public static void main(String[] args) {
		RemoteServerDaemon daemon = new RemoteServerDaemon(args);
		new Thread(daemon).start();

	}

	static void usage(String msg) {
		System.err.println(msg);
		System.err.println("Usage: java " + RemoteServerDaemon.class.getName() + "  configFilePath ");
		System.exit(1);
	}

}
