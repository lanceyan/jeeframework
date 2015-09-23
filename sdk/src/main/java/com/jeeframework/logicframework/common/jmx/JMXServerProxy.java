package com.jeeframework.logicframework.common.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jeeframework.logicframework.common.rmi.RMIServerSocketFactoryWrapper;

public class JMXServerProxy {

	private static final Log LOG = LogFactory
			.getLog(JMXServerProxy.class);
	// ��Щ�������ͨ��-D����ݽ��� spring�����ļ� filter�������Բ��������趨��ȡ��ʽ
	private String serverIp = null; // ������ip
	private int registryPort = 0; // rmiע��������˿�
	private int jmxServerPort = 0; // jmx�������˿�

	public JMXServerProxy(String serverIp, int registryPort,
			int jmxServerPort) {
		this.serverIp = serverIp;
		this.registryPort = registryPort;
		this.jmxServerPort = jmxServerPort;
	}

	public void startServer() {

		String info = "**************  ׼��������������س���   **************";
		System.out.println(info);
		LOG.debug(info);
		if (serverIp == null || serverIp.trim().length() == 0) {
			throw new RuntimeException("�����ü�ط�����IP");
		}
		if (registryPort <= 0) {
			throw new RuntimeException("rmiע��������˿�");
		}
		if (jmxServerPort <= 0) {
			throw new RuntimeException("jmx�������˿�");
		}
		RMIServerSocketFactoryWrapper ssf = new RMIServerSocketFactoryWrapper();
		ssf.setIpAddr(serverIp);
		// LocateRegistry.createRegistry(9001);
		try {
			LocateRegistry.createRegistry(registryPort, null, ssf);

			info = "**************  rmiע������������ɹ�   **************";
			System.out.println(info);
			LOG.debug(info);
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			HashMap env = new HashMap();
			env
					.put(
							RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE,
							ssf);
			String serviceURL = "service:jmx:rmi://" + serverIp + ":"
					+ jmxServerPort + "/jndi/rmi://" + serverIp + ":"
					+ registryPort + "/jmxrmi";
			JMXServiceURL url = new JMXServiceURL(serviceURL);

			JMXConnectorServer cs = JMXConnectorServerFactory
					.newJMXConnectorServer(url, env, mbs);
			cs.start();
			info = "**************  jmx��ط����������ɹ�   ************** \r\n"
					+ serviceURL;
			System.out.println(info);
			LOG.debug(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("jmx��ط�������������", e);
		}

	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	public void setRegistryPort(int registryPort) {
		this.registryPort = registryPort;
	}

	public int getJmxServerPort() {
		return jmxServerPort;
	}

	public void setJmxServerPort(int jmxServerPort) {
		this.jmxServerPort = jmxServerPort;
	}

}
