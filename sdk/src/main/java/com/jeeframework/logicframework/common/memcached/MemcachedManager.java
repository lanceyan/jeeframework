package com.jeeframework.logicframework.common.memcached;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.util.validate.Validate;

public class MemcachedManager implements ContextManageBean {

//	MemcachedManager memcachedManager = MemcachedManager.getInstance();
//	
//	memcachedManager.add("test", "aaaaa");
//	
//	String aa = (String)memcachedManager.get("test");
	
	
	protected MemCachedClient mcc = new MemCachedClient();

	private static MemcachedManager memcachedManager = new MemcachedManager();

	public static MemcachedManager getInstance() {
		return memcachedManager;
	}

	// 创建 memcached连接池

	// 指定memcached服务地址
	// String[] servers = { "server1.mydomain.com:1121",
	// "server2.mydomain.com:1121", "server3.mydomain.com:1121" };
	private String servers = "192.168.0.237:12000";

	// 指定memcached服务器负载量
	// Integer[] weights = { 3, 3, 2 };
	private String weights = "3";
	// 从连接池获取一个连接实例
	// 设置初始连接数5 最小连接数 5 最大连接数 250
	private int initConn = 5;

	private int minConn = 5;

	private int maxConn = 250;

	// 设置一个连接最大空闲时间6小时
	private int maxIdle = 1000 * 60 * 60 * 6;

	// 设置主线程睡眠时间

	// 每隔30秒醒来 然后

	// 开始维护 连接数大小
	private int maintSleep = 30;

	// 设置tcp 相关的树形

	// 关闭nagle算法

	// 设置 读取 超时3秒钟 set the read timeout to 3 secs

	// 不设置连接超时

	public MemCachedClient getMcc() {
		return mcc;
	}

	public void setMcc(MemCachedClient mcc) {
		this.mcc = mcc;
	}


	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public String getWeights() {
		return weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public int getInitConn() {
		return initConn;
	}

	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}

	public int getMinConn() {
		return minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaintSleep() {
		return maintSleep;
	}

	public void setMaintSleep(int maintSleep) {
		this.maintSleep = maintSleep;
	}

	public boolean isNagle() {
		return isNagle;
	}

	public void setIsNagle(boolean isNagle) {
		this.isNagle = isNagle;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	private boolean isNagle = false;

	private int readTimeOut = 3000;

	private int connectTimeOut = 0;

	// 设置压缩模式

	// 如果超过64k压缩数据

	// mcc.setCompressEnable(true);
	// mcc.setCompressThreshold(64 * 1024);

	/**
	 * 添加一个指定的值到缓存中.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value) {
		return mcc.add(key, value);
	}

	public boolean add(String key, Object value, Date expiry) {
		return mcc.add(key, value, expiry);
	}

	/**
	 * 替换一个指定的值到缓存中.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value) {
		return mcc.replace(key, value);
	}

	public boolean replace(String key, Object value, Date expiry) {
		return mcc.replace(key, value, expiry);
	}

	/**
	 * 删除一个指定的值到缓存中.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean delete(String key) {
		return mcc.delete(key);
	}

	/**
	 * 根据指定的关键字获取对象.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return mcc.get(key);
	}

	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub

	}

	
	public void setBeanName(String name) {
		// TODO Auto-generated method stub

	}

	
	public void afterPropertiesSet() throws Exception {

		SockIOPool pool = SockIOPool.getInstance();

		// 设置服务器和服务器负载量

		if (!Validate.isEmpty(servers)) {

			String[] serverArray = servers.split(",");

			pool.setServers(serverArray);
		}
		if (!Validate.isEmpty(weights)) {
			String[] weightArray = weights.split(",");

			List<Integer> weightList = new ArrayList<Integer>();
			for (String weightStr : weightArray) {
				Integer intWeight = Integer.valueOf(weightStr);
				weightList.add(intWeight);
			}

			pool.setWeights(weightList.toArray(new Integer[0]));
		}

		// 设置一些基本的参数

		pool.setInitConn(initConn);
		pool.setMinConn(minConn);
		pool.setMaxConn(maxConn);
		pool.setMaxIdle(maxIdle);

		pool.setMaintSleep(maintSleep);

		// 设置tcp 相关的树形

		// 关闭nagle算法

		// 设置 读取 超时3秒钟 set the read timeout to 3 secs

		// 不设置连接超时
		

		pool.setNagle(isNagle);
		pool.setSocketTO(readTimeOut);
		pool.setSocketConnectTO(connectTimeOut);
		// 开始初始化 连接池

		pool.initialize();

	}

	
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

}
