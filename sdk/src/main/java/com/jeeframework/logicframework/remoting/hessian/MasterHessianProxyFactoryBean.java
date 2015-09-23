package com.jeeframework.logicframework.remoting.hessian;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.SerializerFactory;
import com.jeeframework.core.exception.BaseException;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ���Ķ�hessian���?�����еķ�װ�� <br>
 * ������������hessian��Զ�̽ӿڶ���֧�ִ��������Ļ�ȡip�� <br>
 * ���ڸ��ؾ��͹���ת�ơ�
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see HessianProxyFactoryBean
 */

public class MasterHessianProxyFactoryBean extends HessianProxyFactoryBean {
	/**
	 * ������ʴ������
	 */
	private Object serviceProxy;

	/**
	 * �Ƿ���������Ļ�ȡ������ip�б�
	 */
	private boolean isConfig = false; // 是否开启master切换机制，没有就走普通的hessian调用方式

	public boolean isConfig() {
		return isConfig;
	}

	public void setIsConfig(boolean isConfig) {
		this.isConfig = isConfig;
	}

	private int retryCount = 3;
	private MasterHessianProxyObject curMasterHessianProxyObject;

	/**
	 * �������ĵķ�������ַ���б�
	 */
	private ArrayList<InetSocketAddress> serverList = null;

	/**
	 * hessianProxy��map�����Է���urlΪkey��hessianProxyΪvalue
	 */
	private Map<String, Object> hessianProxyMap = new HashMap<String, Object>();

	/**
	 * �滻url��remote.ip�ַ�
	 */
	private static final String REMOTE_SERVER_IP = "#remote.ip#";

	/**
	 * �滻url��remote.port�ַ�
	 */
	private static final String REMOTE_SERVER_PORT = "#remote.port#";

	// urlΪkey
	/**
	 * ���url����hessianProxy�Ĺ�����
	 */
	private HessianProxyFactory proxyFactory = new HessianProxyFactory();

	/**
	 * ���url�����hessianProxy
	 */
	private Object hessianProxy;

	/**
	 * ���ʵķ�����ip��ַ�б�
	 */
	private String serverIPList = null;

	public void afterPropertiesSet() {
		// ������������ķ�ʽ�������ر�������Ƿ�����
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}

	public Object getObject() {
		return this.serviceProxy;
	}

	public Class getObjectType() {
		return getServiceInterface();
	}

	/**
	 * Ϊfalseÿ�η��ض������getObject������ <br>
	 * ����ֻ����һ��getObject��
	 * 
	 * @return <code>false</code>
	 * @see org.springframework.remoting.caucho.HessianProxyFactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Initialize the Hessian proxy for this interceptor.
	 * 
	 * @throws RemoteLookupFailureException
	 *             if the service URL is invalid
	 */
	public void prepare() throws RemoteLookupFailureException {
		try {
			MasterHessianProxy mhProxy = MasterHessianProxy.getInstance();

			// ���񷽵�ip�б�Ϊ�գ����ߵ�ip��port��ʽ
			if (mhProxy.isProxy()) {

				// �б?Ϊ�����ip�б�õ�ip��port����ʼ��hessianProxyMap
				// for (InetSocketAddress serverIPAddr : serverList) {
				// String serviceUrl = getServiceUrl(serverIPAddr);
				//
				// Object hessianProxy = createHessianProxy(this.proxyFactory,
				// serviceUrl);
				// hessianProxyMap.put(serviceUrl, hessianProxy);
				// }

				if (null == curMasterHessianProxyObject) {
					curMasterHessianProxyObject = mhProxy.getCurMasterHessianProxyObject();
				}

				InetSocketAddress serverIPAddr = new InetSocketAddress(curMasterHessianProxyObject.getServerIp(), curMasterHessianProxyObject.getServerPort());
				String serviceUrl = getServiceUrl(serverIPAddr);

				this.hessianProxy = createHessianProxy(this.proxyFactory, serviceUrl);
				// Object hessianProxy = createHessianProxy(this.proxyFactory,
				// serviceUrl);
				// hessianProxyMap.put(serviceUrl, hessianProxy);

			} else
			// serverList
			// ��װ��beanʱִ��setServerIPList��������initSvcAddress��ʼ����
			{
				this.hessianProxy = createHessianProxy(this.proxyFactory);
			}
		} catch (MalformedURLException ex) {
			throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
		}
	}

	/**
	 * Create the Hessian proxy that is wrapped by this interceptor.
	 * 
	 * @param proxyFactory
	 *            the proxy factory to use
	 * @return the Hessian proxy
	 * @throws MalformedURLException
	 *             if thrown by the proxy factory
	 * @see com.caucho.hessian.client.HessianProxyFactory#create
	 */
	protected Object createHessianProxy(HessianProxyFactory proxyFactory) throws MalformedURLException {
		Assert.notNull(getServiceInterface(), "'serviceInterface' is required");
		return proxyFactory.create(getServiceInterface(), getServiceUrl());
	}

	protected Object createHessianProxy(HessianProxyFactory proxyFactory, String serviceUrl) throws MalformedURLException {
		Assert.notNull(getServiceInterface(), "'serviceInterface' is required");
		return proxyFactory.create(getServiceInterface(), serviceUrl);
	}

	/**
	 * ���ip�б�õ����ʵķ�������ַ�� �������ǰ�ж���ipAndPort�Ƿ�Ϊnull�������ظ�У�顣
	 * ��ַ��ʽΪ��ip1,ip2:80
	 * 
	 * @param ipAndPorts
	 *            ���ip�б�õ����ʵķ�������ַ
	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ��������
	 */
	public static synchronized ArrayList<InetSocketAddress> initSvcAddress(String ipAndPorts) {

		ArrayList<InetSocketAddress> serverList = new ArrayList<InetSocketAddress>();

		ipAndPorts = ipAndPorts.trim();
		if (ipAndPorts != null && ipAndPorts.length() > 0) {
			String[] arraystr = ipAndPorts.split(",");

			// �ָ�� ���� ip�б� �� �˿�
			if (arraystr != null && arraystr.length > 0) {

				for (String ipAndPort : arraystr) {

					String[] ipAndPortArr = ipAndPort.split(":");
					String serverIp = ipAndPortArr[0].trim();
					String portStr = ipAndPortArr[1].trim();
					int port = Integer.parseInt(portStr);
					InetSocketAddress ret = new InetSocketAddress(serverIp, port);
					serverList.add(ret);
				}
			}
		}
		return serverList;
	}

	/**
	 * aop���õķ�����
	 * 
	 * @param invocation
	 *            aop���õķ�����
	 * @return Object aop��ʽ���÷������صĽ��
	 * @see org.springframework.remoting.caucho.HessianClientInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {

		String serviceUrl = null;
		// // �����˱���serverIP�б?���hessianProxyMap�������ȡ
		// if (serverIPList != null && serverIPList.trim().length() > 0) {
		//
		// // ip列表
		// InetSocketAddress configSocketAddr = serverList.get(0);
		//
		// serviceUrl = getServiceUrl(configSocketAddr);
		//
		// hessianProxy = hessianProxyMap.get(serviceUrl);
		// }

		// û�����ã��ߵ�ip��port��ʽ

		if (this.hessianProxy == null) {
			throw new IllegalStateException("HessianClientInterceptor is not properly initialized - " + "invoke 'prepare' before attempting any operations");
		}

		Object retObject = null;

		int retry = 0;
		boolean whileCondition = true;
		while (whileCondition) {

			ClassLoader originalClassLoader = overrideThreadContextClassLoader();
			try {
				retObject = invocation.getMethod().invoke(this.hessianProxy, invocation.getArguments());
				break;
			} catch (InvocationTargetException ex) {
				if (ex.getTargetException() instanceof HessianRuntimeException) {
					HessianRuntimeException hre = (HessianRuntimeException) ex.getTargetException();
					Throwable rootCause = (hre.getRootCause() != null ? hre.getRootCause() : hre);

					if (rootCause instanceof ConnectException) {

						if (retry >= retryCount) {

							curMasterHessianProxyObject.setStatus(MasterHessianProxy.REMOTE_MASTER_STATUS_ERROR);

							MasterHessianProxy mhProxy = MasterHessianProxy.getInstance();
							curMasterHessianProxyObject = mhProxy.getCurMasterHessianProxyObject();

							InetSocketAddress serverIPAddr = new InetSocketAddress(curMasterHessianProxyObject.getServerIp(), curMasterHessianProxyObject.getServerPort());
							String newServiceUrl = getServiceUrl(serverIPAddr);

							this.hessianProxy = createHessianProxy(this.proxyFactory, newServiceUrl);

							retry = 0;

							continue;
						}

						System.out.println("RetryCount is " + retry + " +, Cannot connect to Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]");
						rootCause.printStackTrace();

						// return new
						// RemoteConnectFailureException("Cannot connect to Hessian remote service at ["
						// + (serviceUrl == null ? getServiceUrl() : serviceUrl)
						// + "]", rootCause);
					} else {
						throw new RemoteAccessException("Cannot access Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]", rootCause);
					}

					// throw convertHessianAccessException(rootCause,
					// serviceUrl);
				} else if (ex.getTargetException() instanceof UndeclaredThrowableException) {
					UndeclaredThrowableException utex = (UndeclaredThrowableException) ex.getTargetException();

					Throwable utexCause = utex.getUndeclaredThrowable();
					if (utexCause instanceof ConnectException) {

						if (retry >= retryCount) {

							curMasterHessianProxyObject.setStatus(MasterHessianProxy.REMOTE_MASTER_STATUS_ERROR);

							MasterHessianProxy mhProxy = MasterHessianProxy.getInstance();
							curMasterHessianProxyObject = mhProxy.getCurMasterHessianProxyObject();

							InetSocketAddress serverIPAddr = new InetSocketAddress(curMasterHessianProxyObject.getServerIp(), curMasterHessianProxyObject.getServerPort());
							String newServiceUrl = getServiceUrl(serverIPAddr);

							this.hessianProxy = createHessianProxy(this.proxyFactory, newServiceUrl);

							retry = 0;

							continue;
						}

						System.out.println("RetryCount is " + retry + " +, Cannot connect to Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]");
						utexCause.printStackTrace();

						// return new
						// RemoteConnectFailureException("Cannot connect to Hessian remote service at ["
						// + (serviceUrl == null ? getServiceUrl() : serviceUrl)
						// + "]", utexCause);
					} else {
						throw new RemoteAccessException("Cannot access Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]", utexCause);
					}

					// throw
					// convertHessianAccessException(utex.getUndeclaredThrowable(),
					// serviceUrl);
				}

				throw ex.getTargetException();
			} catch (Throwable ex) {
				throw new RemoteProxyFailureException("Failed to invoke Hessian proxy for remote service [" + getServiceUrl() + "]    " + (serviceUrl == null ? "" : serviceUrl), ex);
			} finally {
				resetThreadContextClassLoader(originalClassLoader);

				retry++;
			}

		}

		return retObject;

	}

	/**
	 * ͨ���ƶ�InetSocketAddressȡ��ip��port�滻remote.ip��remote.port��ɵ�url��
	 * 
	 * @param configSocketAddr
	 * @return serviceUrl ���ط�����ʵ���url����ͨ��address�����滻���
	 */
	private String getServiceUrl(InetSocketAddress configSocketAddr) {
		String serverIp = configSocketAddr.getAddress().getHostAddress();
		int serverPort = configSocketAddr.getPort();

		// String serverIp = "127.0.0.1";
		// int serverPort = 80;

		String serviceUrl = getServiceUrl();
		// ��ȡremote.ip �� remote.port�����滻
		serviceUrl = serviceUrl.replaceFirst(REMOTE_SERVER_IP, serverIp);
		serviceUrl = serviceUrl.replaceFirst(REMOTE_SERVER_PORT, String.valueOf(serverPort));
		return serviceUrl;
	}

	/**
	 * Convert the given Hessian access exception to an appropriate Spring
	 * RemoteAccessException.
	 * 
	 * @param ex
	 *            the exception to convert
	 * @return the RemoteAccessException to throw
	 */
	protected RemoteAccessException convertHessianAccessException(Throwable ex, String serviceUrl) {
		if (ex instanceof ConnectException) {
			return new RemoteConnectFailureException("Cannot connect to Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]", ex);
		} else {
			return new RemoteAccessException("Cannot access Hessian remote service at [" + (serviceUrl == null ? getServiceUrl() : serviceUrl) + "]", ex);
		}
	}

	/**
	 * ����hessian���?����
	 * 
	 */
	public void setProxyFactory(HessianProxyFactory proxyFactory) {
		this.proxyFactory = (proxyFactory != null ? proxyFactory : new HessianProxyFactory());
	}

	/**
	 * ����hessian���л�������
	 */
	public void setSerializerFactory(SerializerFactory serializerFactory) {
		this.proxyFactory.setSerializerFactory(serializerFactory);
	}

	/**
	 * �����Ƿ���java�������͡�Ĭ��Ϊtrue��
	 */
	public void setSendCollectionType(boolean sendCollectionType) {
		this.proxyFactory.getSerializerFactory().setSendCollectionType(sendCollectionType);
	}

	/**
	 * �Ƿ����ط������Ա�Զ�̵��á�Ĭ��Ϊfalse��
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setOverloadEnabled
	 */
	public void setOverloadEnabled(boolean overloadEnabled) {
		this.proxyFactory.setOverloadEnabled(overloadEnabled);
	}

	/**
	 * ���÷���Զ�̷�����û���Ĭ��Ϊnone��
	 * <p>
	 * username ���ú󽫻ᱻhessian���͵������ͨ�� HTTP Basic AuthenticationУ�顣
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setUser
	 */
	public void setUsername(String username) {
		this.proxyFactory.setUser(username);
	}

	/**
	 * ���÷���Զ�̷�������롣Ĭ��Ϊnone��
	 * <p>
	 * username ���ú󽫻ᱻhessian���͵������ͨ�� HTTP Basic AuthenticationУ�顣
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setPassword
	 */
	public void setPassword(String password) {
		this.proxyFactory.setPassword(password);
	}

	/**
	 * �����Ƿ�hessian��debugģʽ�����á�Ĭ��Ϊfalse��
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setDebug
	 */
	public void setDebug(boolean debug) {
		this.proxyFactory.setDebug(debug);
	}

	/**
	 * �����Ƿ�ʹ�� a chunked post ��������
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setChunkedPost
	 */
	public void setChunkedPost(boolean chunkedPost) {
		this.proxyFactory.setChunkedPost(chunkedPost);
	}

	/**
	 * ���õȴ���񷵻ص���Ӧʱ�䡣���롣
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setReadTimeout
	 */
	public void setReadTimeout(long timeout) {
		this.proxyFactory.setReadTimeout(timeout);
	}

	/**
	 * �����Ƿ��������Ӧʹ��hessian2Э�顣Ĭ��Ϊfalse��
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Request
	 */
	public void setHessian2(boolean hessian2) {
		this.proxyFactory.setHessian2Request(hessian2);
		this.proxyFactory.setHessian2Reply(hessian2);
	}

	/**
	 * �����Ƿ�����ʹ��hessian2Э�顣Ĭ��Ϊfalse��
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Request
	 */
	public void setHessian2Request(boolean hessian2) {
		this.proxyFactory.setHessian2Request(hessian2);
	}

	/**
	 * �����Ƿ���Ӧʹ��hessian2Э�顣Ĭ��Ϊfalse��
	 * 
	 * @see com.caucho.hessian.client.HessianProxyFactory#setHessian2Reply
	 */
	public void setHessian2Reply(boolean hessian2) {
		this.proxyFactory.setHessian2Reply(hessian2);
	}

	/**
	 * ������õķ�����ip�б�
	 * 
	 * @return the serverIpList
	 */
	public String getServerIPList() {
		return serverIPList;
	}

	/**
	 * �������÷����ip server�б�
	 * 
	 * @param serverIPList
	 *            hessian��������ַ
	 */
	public void setServerIPList(String serverIPList) {
		this.serverIPList = serverIPList;
		if (serverIPList != null && serverIPList.trim().length() <= 0) {
			throw new BaseException("serverIPList没有指定");
		}
		if (serverIPList != null && serverIPList.trim().length() > 0) {
			serverList = initSvcAddress(serverIPList);
			if (serverList.size() == 0) {
				throw new BaseException("serverIPList格式不正确，使用 ip:port 或者  ip1,ip2,ip3:port");
			}
		}

	}

}
