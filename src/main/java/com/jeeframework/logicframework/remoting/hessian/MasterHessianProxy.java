package com.jeeframework.logicframework.remoting.hessian;

import com.jeeframework.logicframework.util.server.PropertyReaderUtil;
import org.springframework.remoting.RemoteLookupFailureException;


/**
 * 
 * @author sven
 */
public class MasterHessianProxy {
	private static MasterHessianProxy instance = null;

	public static final int REMOTE_MASTER = 1;
	public static final int REMOTE_BAKMASTER = 2;

	public static final int REMOTE_MASTER_STATUS_UNDO = 0;// 未使用
	public static final int REMOTE_MASTER_STATUS_DOING = 1;// 正使用
	public static final int REMOTE_MASTER_STATUS_ERROR = 9;// 使用错误，可能机器挂掉

	// private MasterHessianProxyObject curMasterHessianProxyObject = null;
	private MasterHessianProxyObject masterHessianProxyObject = null;// 主master
	private MasterHessianProxyObject bakMasterHessianProxyObject = null;// 备master

	private boolean isProxy = false; // 是否开启master切换机制，没有就走普通的hessian调用方式

	// private ArrayList<MasterHessianProxyObject> masterHessianProxyObjectList
	// = new ArrayList<MasterHessianProxyObject>();

	public MasterHessianProxy() {
		super();

		init();
	}

	public static synchronized MasterHessianProxy getInstance() {
		if (null == instance) {
			instance = new MasterHessianProxy();
		}

		return instance;
	}

	public void init() {

		isProxy = Boolean.valueOf(PropertyReaderUtil.getValue("server.proxy"));

		String masterIp = PropertyReaderUtil.getValue("server.master.ip");
		int masterPort = 0;
		try {
			masterPort = Integer.parseInt(PropertyReaderUtil.getValue("server.master.port"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		masterHessianProxyObject = new MasterHessianProxyObject(masterIp, masterPort, REMOTE_MASTER_STATUS_UNDO, REMOTE_MASTER);

		// 默认最先开始使用的是主master
		masterHessianProxyObject.setStatus(REMOTE_MASTER_STATUS_DOING);

		// MasterHessianProxyObject mpo = new MasterHessianProxyObject();
		// mpo.setServerIp(masterIp);
		// mpo.setServerPort(masterPort);
		// mpo.setServerFlag(REMOTE_MASTER);
		// masterHessianProxyObjectList.add(mpo);

		String bakMasterIp = PropertyReaderUtil.getValue("server.bakmaster.ip");
		int bakMasterPort = 0;
		try {
			bakMasterPort = Integer.parseInt(PropertyReaderUtil.getValue("server.bakmaster.port"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		bakMasterHessianProxyObject = new MasterHessianProxyObject(bakMasterIp, bakMasterPort, REMOTE_MASTER_STATUS_UNDO, REMOTE_BAKMASTER);

		// MasterHessianProxyObject bmpo = new MasterHessianProxyObject();
		// bmpo.setServerIp(bakMasterIp);
		// bmpo.setServerPort(bakMasterPort);
		// bmpo.setServerFlag(REMOTE_BAKMASTER);
		// masterHessianProxyObjectList.add(bmpo);

	}

	public synchronized MasterHessianProxyObject getCurMasterHessianProxyObject() {
		// if (null == masterHessianProxyObject) {
		// for (MasterHessianProxyObject mpo : masterHessianProxyObjectList) {
		// if (mpo.getServerFlag() == REMOTE_MASTER && mpo.getStatus() !=
		// REMOTE_MASTER_STATUS_ERROR) {
		// masterHessianProxyObject = mpo;
		// masterHessianProxyObject.setStatus(REMOTE_MASTER_STATUS_DOING);
		// break;
		// }
		// }
		// }

		/*
		 * 主master down掉，切换备master并且发主master down掉邮件<br>
		 * 如果备master也挂掉的话，发送紧急通知邮件并且抛出没有master可使用了异常
		 */
		if (masterHessianProxyObject.getStatus() == REMOTE_MASTER_STATUS_ERROR) {

			if (bakMasterHessianProxyObject.getStatus() == REMOTE_MASTER_STATUS_UNDO) {
				bakMasterHessianProxyObject.setStatus(REMOTE_MASTER_STATUS_DOING);
				return bakMasterHessianProxyObject;
			}

			if (bakMasterHessianProxyObject.getStatus() == REMOTE_MASTER_STATUS_ERROR) {
				// return null;
				throw new RemoteLookupFailureException("当前主master和备master都挂掉了，无法使用远程服务，请快恢复!");
			}

		}

		// 主master是可以使用的，返回主master
		return masterHessianProxyObject;
		// if (null == curMasterHessianProxyObject) {
		// masterHessianProxyObject.setStatus(REMOTE_MASTER_STATUS_DOING);
		//
		// curMasterHessianProxyObject = masterHessianProxyObject;

		// if (masterHessianProxyObject.getStatus() ==
		// REMOTE_MASTER_STATUS_ERROR) {
		// if (bakMasterHessianProxyObject.getStatus() ==
		// REMOTE_MASTER_STATUS_ERROR) {
		// throw Exception
		// } else {
		// curMasterHessianProxyObject = bakMasterHessianProxyObject;
		// }
		// } else {
		// curMasterHessianProxyObject = masterHessianProxyObject;
		// }
		// }
	}

	public boolean isProxy() {
		return isProxy;
	}

	public static void main(String[] args) {
		// System.out.println(Integer.getInteger(0));
	}

}
