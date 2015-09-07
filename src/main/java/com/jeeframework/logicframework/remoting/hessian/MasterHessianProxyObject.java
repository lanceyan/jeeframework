package com.jeeframework.logicframework.remoting.hessian;

/**
 * 
 * @author sven
 */
public class MasterHessianProxyObject {
	private String serverIp;
	private Integer serverPort;
	private Integer status;
	private Integer serverFlag;

	public MasterHessianProxyObject() {
		super();
	}

	public MasterHessianProxyObject(String serverIp, Integer serverPort, Integer status, Integer serverFlag) {
		super();
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.status = status;
		this.serverFlag = serverFlag;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getServerFlag() {
		return serverFlag;
	}

	public void setServerFlag(Integer serverFlag) {
		this.serverFlag = serverFlag;
	}
}
