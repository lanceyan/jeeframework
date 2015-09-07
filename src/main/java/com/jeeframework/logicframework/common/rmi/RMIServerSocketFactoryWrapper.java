package com.jeeframework.logicframework.common.rmi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

/** 
 * rmiserver�Ĺ�����
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */
	
public class RMIServerSocketFactoryWrapper implements RMIServerSocketFactory {

	/**
	 * �󶨵ĵ�ַ
	 */
	private String ipAddr = null;
	/**
	 * ������и������socket��backlog��Ĭ��50��
	 */
	private int reqQueue = 50; // �������Ĭ��50��

	/**
	 * ͨ��port����ServerSocket������rmi����
	 * @param port  ָ��port����socket
	 * @return ServerSocket�������ķ����
	 * @exception IOException
	 * @see java.rmi.server.RMIServerSocketFactory#createServerSocket(int)
	 */
	
	public ServerSocket createServerSocket(int port) throws IOException {

		if (ipAddr == null) {
			return new ServerSocket(port, reqQueue);
		} else {
			InetAddress inet = InetAddress.getByName(ipAddr);
			return new ServerSocket(port, reqQueue, inet);
		}

	}

	/**
	 * ���ذ󶨵�ip��ַ
	 * @return ipAddr������˰󶨵�ip��ַ
	 */
	public String getIpAddr() {
		return ipAddr;
	}
    /**
     * ���ð󶨵�ip��ַ
     * @param  ipAddr ��Ҫ�󶨵�ip��ַ
     */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * �õ�����Ķ��и���
	 * @return reqQueue ����Ķ���
	 */
	public int getReqQueue() {
		return reqQueue;
	}
    /**
     * ��������Ķ��и���
     * @param  reqQueue  ����Ķ��и���
     */
	public void setReqQueue(int reqQueue) {
		this.reqQueue = reqQueue;
	}

}
