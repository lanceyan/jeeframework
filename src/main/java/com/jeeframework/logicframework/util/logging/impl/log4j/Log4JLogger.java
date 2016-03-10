//package com.jeeframework.logicframework.common.logging.impl.log4j;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Appender;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PatternLayout;
//import org.apache.log4j.RollingFileAppender;
//import org.apache.log4j.net.SocketAppender;
//import org.springframework.beans.factory.InitializingBean;
//
//import BaseException;
//import BizException;
//import LoggerUtil;
//import FileUtils;
//
///**
// * ������log4j��־���ĸ���־���� <li>���� debug</li> <li>���� performance</li> <li>
// * ��Ӫ statistic</li> <li>���� error</li>
// *
// * <p>
// * ������һ��log��¼�쳣�Ķ�ջ��Ϣ
// *
// * @author lanceyan�������޸��ߣ�
// * @version 1.0���°汾�ţ�
// *
// */
//
//public class Log4JLogger implements Logger, InitializingBean {
//	// private static final String FQCN = Log4JLogger.class.getName();
//
//	// 7 trace 7 debug 6 info 4 warn 3 error 0 fatal
//
//	public final static String TRANSING_LOG_SPLIT = "_trans_";
//
//	private String logLevel = "debug";
//
//	static String MONITORLOG = "monitor.log";
//
//	public final static Map<String, Level> logLevelMap = new HashMap<String, Level>();
//
//	static {
//
//		logLevelMap.put("trace", Level.TRACE);
//		logLevelMap.put("debug", Level.DEBUG);
//		logLevelMap.put("info", Level.INFO);
//		logLevelMap.put("warn", Level.WARN);
//		logLevelMap.put("error", Level.ERROR);
//		logLevelMap.put("fatal", Level.FATAL);
//
//	}
//
//	private Logger logger = null;
//
//	private String name = null;
//
//	// ϵͳ����c2c��b2c
//	private String systemName = null;
//
//	private String logServerIp = null; // log �������ĵ�ַ
//
//	private int logServerPort; // log �������Ķ˿�
//
//	private String maxFileSize = "10MB"; // �����ļ�size ��λ�� KB/MB/GB��Ĭ��Ϊ10MB
//
//	private int maxBackupIndex = 10; // ����ļ����ݸ���.Ĭ��Ϊ10��
//
//	private int maxBackupDateIndex = 10; // ��������ļ�����.Ĭ��Ϊ10��
//
//	private String rootLoggerName = "rootLogger.log"; // 根日志的配置文件
//
//	public String getRootLoggerName() {
//		return rootLoggerName;
//	}
//
//	public void setRootLoggerName(String rootLoggerName) {
//		this.rootLoggerName = rootLoggerName;
//	}
//
//	public int getMaxBackupDateIndex() {
//		return maxBackupDateIndex;
//	}
//
//	public void setMaxBackupDateIndex(int maxBackupDateIndex) {
//		this.maxBackupDateIndex = maxBackupDateIndex;
//	}
//
//	private String logDirPath = null; // log��λ��
//
//	InetAddress address;
//
//	SocketAppender socketAppender;
//
//	private boolean sqlLogOpened = false; // �Ƿ�����Ӫlog��sql log
//
//	private boolean sqlRemoteOpened = false; // 是否开启远程sql日志传递
//
//
//	public boolean isMonitorRemoteLogOpened() {
//		return monitorRemoteLogOpened;
//	}
//
//	public void setMonitorRemoteLogOpened(boolean monitorRemoteLogOpened) {
//		this.monitorRemoteLogOpened = monitorRemoteLogOpened;
//	}
//
//	private boolean monitorRemoteLogOpened = false; // 远程监控日志是否开启
//
//	public boolean isSqlRemoteOpened() {
//		return sqlRemoteOpened;
//	}
//
//	public void setSqlRemoteOpened(boolean sqlRemoteOpened) {
//		this.sqlRemoteOpened = sqlRemoteOpened;
//	}
//
//	public boolean isSqlLogOpened() {
//		return sqlLogOpened;
//	}
//
//	public void setSqlLogOpened(boolean sqlLogOpened) {
//		this.sqlLogOpened = sqlLogOpened;
//	}
//
//	// lance ���� Todo:������ʵ�������Ӧ�ð���Ӫlog��sqllog�Ŀ��Ʒֿ�
//	// ��Ӧ�������Ƿ��ϴ����񿪹ء����� ��Ӫlog�� sqllog������ipΪnull portΪ0��Ϊ���ء�
//	private int moduleInPackageIndex = 0; // ��ȡģ����package�е�λ�ã����ڸ��package���ģ��
//
//	private boolean outDebug = false;
//
//	public Log4JLogger() {
//	}
//
//	public Log4JLogger(String name) {
//		this.name = name;
//		this.logger = getLogger();
//	}
//
//	public Log4JLogger(Logger logger) {
//		this.name = logger.getName();
//		this.logger = logger;
//	}
//
//	public Logger getLogger() {
//		if (logger == null) {
//			logger = Logger.getLogger(name);
//		}
//		return (this.logger);
//	}
//
//	public String getName() {
//		return this.name;
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>DEBUG</code>
//	 * priority.
//	 */
//	public boolean isDebugEnabled() {
//		// return outDebug;
//		return getLogger().isDebugEnabled();
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>ERROR</code>
//	 * priority.
//	 */
//	public boolean isErrorEnabled() {
//		return getLogger().isEnabledFor(Level.ERROR);
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>FATAL</code>
//	 * priority.
//	 */
//	public boolean isFatalEnabled() {
//		return getLogger().isEnabledFor(Level.FATAL);
//
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>INFO</code>
//	 * priority.
//	 */
//	public boolean isInfoEnabled() {
//		return getLogger().isInfoEnabled();
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>TRACE</code>
//	 * priority. For Log4J, this returns the value of
//	 * <code>isDebugEnabled()</code>
//	 */
//	public boolean isTraceEnabled() {
//		return getLogger().isDebugEnabled();
//	}
//
//	/**
//	 * Check whether the Log4j Logger used is enabled for <code>WARN</code>
//	 * priority.
//	 */
//	public boolean isWarnEnabled() {
//		return getLogger().isEnabledFor(Level.WARN);
//
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����Լ�����־
//	 *
//	 * @param DBG
//	 *            ���Լ�����־Ҫ��¼����Ϣ ��������
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void debugTrace(String DBG) {
//		Logger logger = Logger.getLogger(LoggerUtil.DEBUG);
//		if (logger == null) {
//			throw new BaseException("������debug logger");
//		}
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.debugLevel);
//		tempMessage.append(DBG);
//
//		logger.debug(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����Լ�����־
//	 *
//	 * @param DBG
//	 *            ���Լ�����־Ҫ��¼����Ϣ ��������
//	 * @param t
//	 *            ���Լ�����־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void debugTrace(String DBG, Throwable t) {
//		Logger logger = Logger.getLogger(LoggerUtil.DEBUG);
//		if (logger == null) {
//			throw new BaseException("������debug logger");
//		}
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.debugLevel);
//		tempMessage.append(DBG);
//
//		logger.debug(tempMessage.toString(), t);
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����ܼ�����־
//	 *
//	 * @param PC
//	 *            ���������
//	 * @param PR
//	 *            �������ϱ�ֵ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void perTrace(String PC, String PR) {
//		logger = Logger.getLogger(LoggerUtil.PERFORMANCE);
//		if (logger == null) {
//			throw new BaseException("������perf logger");
//		}
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.perfLevel);
//		tempMessage.append(PC);
//		tempMessage.append(" ");
//		tempMessage.append(PR);
//		logger.info(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����ܼ�����־
//	 *
//	 * @param PC
//	 *            ���������
//	 * @param PR
//	 *            �������ϱ�ֵ
//	 * @param t
//	 *            ���ܼ�����־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void perTrace(String PC, String PR, Throwable t) {
//		logger = Logger.getLogger(LoggerUtil.PERFORMANCE);
//		if (logger == null) {
//			throw new BaseException("������perf logger");
//		}
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.perfLevel);
//		tempMessage.append(PC);
//		tempMessage.append(" ");
//		tempMessage.append(PR);
//		logger.info(tempMessage.toString(), t);
//	}
//
//	// /**
//	// * ������ ����Ӫͳ����Ϣ��¼����Ӫ������־
//	// *
//	// * @param uin
//	// * �������û���QQ����
//	// * @param RIP
//	// * �����ߵ���ԴIP
//	// * @param port
//	// * �����߶˿�
//	// * @param busiName
//	// * ҵ����
//	// * @param CHR
//	// * ��ע
//	// * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	// * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	// *
//	// */
//	// public void infoTrace(int uin, String RIP, int port, String busiName,
//	// String CHR) {
//	// Logger logger = Logger.getLogger(LoggerUtil.INFO);
//	// if (logger == null) {
//	// throw new BaseException("������info logger");
//	// }
//	// StringBuffer tempMessage =
//	// LoggerUtil.getCommonStringBuffer(LoggerUtil.infoLevel);
//	// tempMessage.append(uin);
//	// tempMessage.append(" ");
//	// tempMessage.append(RIP);
//	// tempMessage.append(" ");
//	// tempMessage.append(port);
//	// tempMessage.append(" ");
//	// tempMessage.append(busiName);
//	// tempMessage.append(" ");
//	// tempMessage.append(CHR);
//	// logger.info(tempMessage.toString());
//	// }
//
//	// /**
//	// * ������ ����Ӫͳ����Ϣ��¼����Ӫ������־
//	// *
//	// * @param uin
//	// * �������û���QQ����
//	// * @param RIP
//	// * �����ߵ���ԴIP
//	// * @param port
//	// * �����߶˿�
//	// * @param busiName
//	// * ҵ����
//	// * @param CHR
//	// * ��ע
//	// * @param t
//	// * ��Ӫ������־��Ҫ��¼���쳣
//	// * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	// * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	// *
//	// */
//	// public void infoTrace(int uin, String RIP, int port, String busiName,
//	// String CHR, Throwable t) {
//	// Logger logger = Logger.getLogger(LoggerUtil.INFO);
//	// if (logger == null) {
//	// throw new BaseException("������info logger");
//	// }
//	// StringBuffer tempMessage =
//	// LoggerUtil.getCommonStringBuffer(LoggerUtil.infoLevel);
//	// tempMessage.append(uin);
//	// tempMessage.append(" ");
//	// tempMessage.append(RIP);
//	// tempMessage.append(" ");
//	// tempMessage.append(port);
//	// tempMessage.append(" ");
//	// tempMessage.append(busiName);
//	// tempMessage.append(" ");
//	// tempMessage.append(CHR);
//	// logger.info(tempMessage.toString(), t);
//	// }
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param message
//	 *            ���󼶱���־Ҫ��¼����Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(Object message) {
//		Logger logger = Logger.getLogger(LoggerUtil.EXCEPTION);
//		if (logger == null) {
//			throw new BaseException("������exception logger");
//		}
//		long l = System.currentTimeMillis();
//		java.text.SimpleDateFormat ISO8601_sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = new Date(l);
//		StringBuffer sb = new StringBuffer(ISO8601_sdf.format(date));
//		sb.append(" ");
//		sb.append(message);
//		logger.error(sb.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param message
//	 *            ���󼶱���־Ҫ��¼����Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(Object message, Throwable t) {
//		Logger logger = Logger.getLogger(LoggerUtil.EXCEPTION);
//		if (logger == null) {
//			throw new BaseException("������exception logger");
//		}
//		long l = System.currentTimeMillis();
//		java.text.SimpleDateFormat ISO8601_sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = new Date(l);
//		StringBuffer sb = new StringBuffer(ISO8601_sdf.format(date));
//		sb.append(" ");
//		sb.append(message);
//		logger.error(sb.toString(), t);
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String errorCode, String errorDesc, Object message) {
//		Logger logger = Logger.getLogger(LoggerUtil.ERROR);
//		if (logger == null) {
//			throw new BaseException("������error logger");
//		}
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		if (message != null) {
//			tempMessage.append(": ");
//			tempMessage.append(message);
//		}
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String errorCode, String errorDesc, Object message, Throwable t) {
//		Logger logger = Logger.getLogger(LoggerUtil.ERROR);
//		if (logger == null) {
//			throw new BaseException("������error logger");
//		}
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		if (message != null) {
//			tempMessage.append(": ");
//			tempMessage.append(message);
//		}
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString(), t);
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String errorCode, String errorDesc) {
//		Logger logger = Logger.getLogger(LoggerUtil.ERROR);
//		if (logger == null) {
//			throw new BaseException("������error logger");
//		}
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String errorCode, String errorDesc, Throwable t) {
//		Logger logger = Logger.getLogger(LoggerUtil.ERROR);
//		if (logger == null) {
//			throw new BaseException("������error logger");
//		}
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString(), t);
//	}
//
//	// /**
//	// * ������ ��˾ͳһ��ҵ����Ӫ��־
//	// *
//	// * @param businessName
//	// * ҵ�����
//	// * @param logContent
//	// * ��־����
//	// * @param subModule
//	// * ��ģ��
//	// * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	// * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	// *
//	// */
//	// public void infoTrace(String businessName, String logContent, String
//	// subModule) {
//	//
//	// if (sdkLogOpened) {
//	// StringBuffer loggerIDStrBuf = new StringBuffer();
//	// if (systemName != null) {
//	// loggerIDStrBuf.append(systemName);
//	// loggerIDStrBuf.append("_");
//	// }
//	// loggerIDStrBuf.append(businessName);
//	// loggerIDStrBuf.append("_");
//	// loggerIDStrBuf.append(subModule);
//	// loggerIDStrBuf.append(".log");
//	//
//	// String loggerID = loggerIDStrBuf.toString();
//	//
//	// logger = Logger.getLogger(loggerID);
//	//
//	// if (logger.getAppender(loggerID) == null) {
//	// try {
//	//
//	// PatternLayout patternLayout = new PatternLayout();
//	// RollingFileAppender fileAppender;
//	// // ��tomcat�Ļ����������ȡ��Ӫlog��Ŀ¼
//	//
//	// if (logDirPath == null || logDirPath.trim().length() <= 0) {
//	// // fileDirPath = "D:/log/newlog";
//	// throw new RuntimeException("û������log��Ŀ¼");
//	// }
//	//
//	// if (!logDirPath.endsWith("//")) {
//	// logDirPath = logDirPath + "//";
//	// }
//	//
//	// String keyLogDirPath = logDirPath;
//	//
//	// keyLogDirPath = keyLogDirPath + "infolog//";
//	//
//	// File fileDir = new File(keyLogDirPath);
//	// if (!fileDir.exists()) {
//	// fileDir.mkdirs();
//	// }
//	//
//	// patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss}%m%n");
//	//
//	// StringBuffer dataStrBuf = new StringBuffer();
//	// dataStrBuf.append(keyLogDirPath);
//	//
//	// dataStrBuf.append(loggerID);
//	//
//	// fileAppender = new RollingFileAppender(patternLayout,
//	// dataStrBuf.toString());
//	//
//	// fileAppender.setMaxFileSize(maxFileSize);
//	// fileAppender.setMaxBackupIndex(maxBackupIndex);
//	//
//	// fileAppender.setName(loggerID);
//	//
//	// // fileAppender.setDatePattern("yyyyMMdd");
//	//
//	// // fileAppender.setFile("d:\\log\\test\\" + businessName);
//	//
//	// // ���logServerIp��Ϊ�գ�����Ҫ���socketAppender
//	// if (logServerIp != null && logServerPort != 0) {
//	// // throw new
//	// // SdkRuntimeException("û������Զ��logserver��ip");
//	//
//	// // throw new
//	// // SdkRuntimeException("û������Զ��logserver��port");
//	//
//	// socketAppender.setLocationInfo(true);
//	// // ��������ӿڵ���ʽ
//	// socketAppender.setLayout(patternLayout);
//	//
//	// logger.addAppender(socketAppender);
//	//
//	// }
//	//
//	// logger.addAppender(fileAppender);
//	//
//	// } catch (IOException e) {
//	// throw new RuntimeException(e);
//	// }
//	//
//	// }
//	//
//	// // "##ip##"
//	// // ��ʽΪ ����##ip##body
//	// StringBuffer strBuf = new StringBuffer();
//	// strBuf.append("##");
//	// strBuf.append(ClientIPThreadResourceManager.getResource());
//	// strBuf.append("##");
//	// strBuf.append(logContent);
//	//
//	// logger.info(strBuf.toString());
//	// } else { // û�����ã����¼������info log��
//	// logger = Logger.getLogger(LoggerUtil.INFO);
//	// if (logger == null) {
//	// throw new BaseException("������info logger");
//	// }
//	// StringBuffer tempMessage =
//	// LoggerUtil.getCommonStringBuffer(LoggerUtil.infoLevel);
//	//
//	// tempMessage.append(logContent);
//	// logger.info(tempMessage.toString());
//	// }
//	// }
//
//	/**
//	 * ������ ��sql��Ϣ��¼�����ܼ�����־
//	 *
//	 * @param sqlNameSpace
//	 *            sql��ʾid
//	 * @param sql
//	 *            ִ�е�sql
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void sqlTrace(String sqlNameSpace, String sql) {
//		if (sqlLogOpened) {
//			StringBuffer loggerIDStrBuf = new StringBuffer();
//			loggerIDStrBuf.append(sqlNameSpace);
//			loggerIDStrBuf.append(".sqllog");
//			String loggerID = loggerIDStrBuf.toString();
//
//			logger = Logger.getLogger(loggerID);
//
//			if (logger.getAppender(loggerID) == null) {
//				synchronized (Log4JLogger.class) {
//					if (logger.getAppender(loggerID) == null) {
//
//						try {
//							PatternLayout patternLayout = new PatternLayout();
//							RollingFileAppender fileAppender;
//
//							if (logDirPath == null || logDirPath.trim().length() <= 0) {
//								// fileDirPath = "D:/log/newlog";
//								throw new RuntimeException("û������log��Ŀ¼");
//							}
//
//							if (!logDirPath.endsWith("//")) {
//								logDirPath = logDirPath + "//";
//							}
//
//							if (!logDirPath.endsWith("//")) {
//								logDirPath = logDirPath + "//";
//							}
//
//							String sqlLogDirPath = logDirPath;
//
//							sqlLogDirPath = sqlLogDirPath + "sqllog//";
//
//							File fileDir = new File(sqlLogDirPath);
//							if (!fileDir.exists()) {
//								fileDir.mkdirs();
//							}
//
//							patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss}%m%n");
//
//							StringBuffer dataStrBuf = new StringBuffer();
//							dataStrBuf.append(sqlLogDirPath);
//
//							dataStrBuf.append(loggerID);
//
//							fileAppender = new RollingFileAppender(patternLayout, dataStrBuf.toString());
//
//							fileAppender.setMaxFileSize(maxFileSize);
//							fileAppender.setMaxBackupIndex(maxBackupIndex);
//
//							fileAppender.setName(loggerID);
//
//							if (sqlRemoteOpened) {
//								// 是否开启远程sql操作
//								// ���logServerIp��Ϊ�գ�����Ҫ���socketAppender
//								if (logServerIp != null && logServerPort != 0) {
//
//									socketAppender.setLocationInfo(true);
//									// ��������ӿڵ���ʽ
//									socketAppender.setLayout(patternLayout);
//
//									logger.addAppender(socketAppender);
//
//								}
//							}
//							logger.addAppender(fileAppender);
//
//						} catch (IOException e) {
//							throw new RuntimeException(e);
//						}
//
//					}
//				}
//
//			}
//
//			logger.info(sql);
//		}
//	}
//
//	public void init() {
//
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����Լ�����־
//	 *
//	 * @param busiModule
//	 *            ģ����
//	 * @param DBG
//	 *            ���Լ�����־Ҫ��¼����Ϣ ��������
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void debugTrace(String busiModule, String DBG) {
//		// logger = Logger.getLogger(busiModule +
//		// Logger.DEBUGLOG);
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.DEBUGLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		// String appenderName = busiModule + "_" +
//		// Logger.DEBUGLOG;
//		initAppender(logger, busiModule, Logger.DEBUGLOG);
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.debugLevel);
//		tempMessage.append(DBG);
//
//		logger.debug(tempMessage.toString());
//	}
//
//	public void infoTrace(String busiModule, String infoString) {
//
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.INFOLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		initAppender(logger, busiModule, Logger.INFOLOG);
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.infoLevel);
//		tempMessage.append(infoString);
//
//		logger.info(tempMessage.toString());
//	}
//
//	public void monitorTrace(String busiModule, String monitorString) {
//
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + MONITORLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		initAppender(logger, busiModule, MONITORLOG);
//
//		StringBuffer tempMessage = new StringBuffer();// LoggerUtil.getCommonStringBuffer(LoggerUtil.infoLevel);
//		tempMessage.append(monitorString);
//
//		logger.info(tempMessage.toString());
//	}
//
//	// /**
//	// * ������ ��������Ϣ��¼�����Լ�����־
//	// *
//	// * @param busiModule
//	// * ģ����
//	// * @param DBG
//	// * ���Լ�����־Ҫ��¼����Ϣ ��������
//	// * @param t
//	// * ���Լ�����־��Ҫ��¼���쳣
//	// * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	// * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	// *
//	// */
//	// public void debugTrace(String busiModule, String DBG, Throwable t) {
//	// // logger = Logger.getLogger(busiModule +
//	// // Logger.DEBUGLOG);
//	//
//	// String loggerName = busiModule +
//	// Logger.DEBUGLOG;
//	// Logger logger = Logger.getLogger(loggerName);
//	//
//	// initAppender(logger, busiModule,
//	// Logger.DEBUGLOG);
//	//
//	// StringBuffer tempMessage =
//	// LoggerUtil.getCommonStringBuffer(LoggerUtil.debugLevel);
//	// tempMessage.append(DBG);
//	//
//	// logger.debug(tempMessage.toString(), t);
//	// }
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param message
//	 *            ���󼶱���־Ҫ��¼����Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	// public void errorTrace(String busiModule, Object message)
//	// {
//	// logger = Logger.getLogger(busiModule +
//	// Logger.ERRORLOG);
//	//
//	// if (logger.getAppender(busiModule) == null)
//	// {
//	// initBusiLog(busiModule, Logger.ERRORLOG);
//	// }
//	// long l = System.currentTimeMillis();
//	// java.text.SimpleDateFormat ISO8601_sdf = new
//	// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	// Date date = new Date(l);
//	// StringBuffer sb = new StringBuffer(ISO8601_sdf.format(date));
//	// sb.append(" ");
//	// sb.append(message);
//	// logger.debug(sb.toString());
//	// }
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param message
//	 *            ���󼶱���־Ҫ��¼����Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	// public void errorTrace(String busiModule, Object message, Throwable t)
//	// {
//	// logger = Logger.getLogger(busiModule +
//	// Logger.ERRORLOG);
//	//
//	// if (logger.getAppender(busiModule) == null)
//	// {
//	// initBusiLog(busiModule, Logger.ERRORLOG);
//	// }
//	// long l = System.currentTimeMillis();
//	// java.text.SimpleDateFormat ISO8601_sdf = new
//	// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	// Date date = new Date(l);
//	// StringBuffer sb = new StringBuffer(ISO8601_sdf.format(date));
//	// sb.append(" ");
//	// sb.append(message);
//	// logger.debug(sb.toString(), t);
//	// }
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String busiModule, String errorCode, String errorDesc, Object message) {
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.ERRORLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		// String appenderName = busiModule + "_" +
//		// Logger.ERRORLOG;
//
//		initAppender(logger, busiModule, Logger.ERRORLOG);
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		if (message != null) {
//			tempMessage.append(": ");
//			tempMessage.append(message);
//		}
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String busiModule, String errorCode, String errorDesc, Object message, Throwable t) {
//
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.ERRORLOG;
//		Logger logger = Logger.getLogger(loggerName);
//		//
//		// // if (logger.getAppender(loggerName) == null) {
//		// initAppender(logger, busiModule,
//		// Logger.ERRORLOG);
//		// // }
//
//		// String appenderName = busiModule + "_" +
//		// Logger.ERRORLOG;
//		// RollingFileAppender fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		//
//		// if (fileAppender == null) {
//		// synchronized (Log4JLogger.class) {
//		// fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		// if (fileAppender == null) {
//		// fileAppender = initAppender(logger, busiModule,
//		// Logger.DEBUGLOG);
//		// }
//		// }
//		// }
//
//		initAppender(logger, busiModule, Logger.ERRORLOG);
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		if (message != null) {
//			tempMessage.append(": ");
//			tempMessage.append(message);
//		}
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString(), t);
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String busiModule, String errorCode, String errorDesc) {
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.ERRORLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		// // if (logger.getAppender(loggerName) == null) {
//		// initAppender(logger, busiModule,
//		// Logger.ERRORLOG);
//		// // }
//
//		// String appenderName = busiModule + "_" +
//		// Logger.ERRORLOG;
//		// RollingFileAppender fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		//
//		// if (fileAppender == null) {
//		// synchronized (Log4JLogger.class) {
//		// fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		// if (fileAppender == null) {
//		// fileAppender = initAppender(logger, busiModule,
//		// Logger.DEBUGLOG);
//		// }
//		// }
//		// }
//
//		initAppender(logger, busiModule, Logger.ERRORLOG);
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString());
//	}
//
//	/**
//	 * ������ ��������Ϣ��¼�����󼶱���־
//	 *
//	 * @param errorCode
//	 *            ������
//	 * @param errorDesc
//	 *            ��������
//	 * @param message
//	 *            ������Ϣ
//	 * @param t
//	 *            ���󼶱���־��Ҫ��¼���쳣
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public void errorTrace(String busiModule, String errorCode, String errorDesc, Throwable t) {
//		String loggerName = busiModule + TRANSING_LOG_SPLIT + Logger.ERRORLOG;
//		Logger logger = Logger.getLogger(loggerName);
//
//		// // if (logger.getAppender(loggerName) == null) {
//		// initAppender(logger, busiModule,
//		// Logger.ERRORLOG);
//		// // }
//
//		// String appenderName = busiModule + "_" +
//		// Logger.ERRORLOG;
//		// RollingFileAppender fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		//
//		// if (fileAppender == null) {
//		// synchronized (Log4JLogger.class) {
//		// fileAppender = (RollingFileAppender)
//		// logger.getAppender(appenderName);
//		// if (fileAppender == null) {
//		// fileAppender = initAppender(logger, busiModule,
//		// Logger.DEBUGLOG);
//		// }
//		// }
//		// }
//
//		initAppender(logger, busiModule, Logger.ERRORLOG);
//
//		StackTraceElement frame = LoggerUtil.getRootCauseStack(Thread.currentThread().getStackTrace(), this.getClass().getName());
//
//		String fileName = "";
//		int lineNum = 0;
//		String funcName = "";
//		if (frame != null) {
//			fileName = frame.getClassName();
//			funcName = frame.getMethodName();
//			lineNum = frame.getLineNumber();
//		}
//
//		StringBuffer tempMessage = LoggerUtil.getCommonStringBuffer(LoggerUtil.errorLevel);
//		tempMessage.append(fileName);
//		tempMessage.append(" ");
//		tempMessage.append(lineNum);
//		tempMessage.append(" ");
//		tempMessage.append(funcName);
//		tempMessage.append(" ");
//		tempMessage.append(errorCode);
//		tempMessage.append(" ");
//		tempMessage.append(errorDesc);
//		tempMessage.append(" ");
//		logger.error(tempMessage.toString(), t);
//	}
//
//	/**
//	 * @return the logServerIp
//	 */
//	public String getLogServerIp() {
//		return logServerIp;
//	}
//
//	/**
//	 * @param logServerIp
//	 *            the logServerIp to set
//	 */
//	public void setLogServerIp(String logServerIp) {
//		this.logServerIp = logServerIp;
//	}
//
//	/**
//	 * ��ʼ����ķ�����������ʼ���h��logserver
//	 *
//	 * @param ����˵�� ��ÿ������һ�У�ע����ȡֵ��Χ��
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
//	 */
//	public void afterPropertiesSet() throws Exception {
//		// if (sdkLogOpened) {
//		try {
//			String appRunLogDir = System.getProperty("log.dir");
//
//			if (appRunLogDir != null && appRunLogDir.trim().length() > 0) {
//				logDirPath = appRunLogDir;
//			}
//
//			if (logServerIp != null && logServerPort != 0) {
//
//				address = InetAddress.getByName(logServerIp);
//
//				socketAppender = new SocketAppender(address, logServerPort);
//
//			}
//
//			if (logDirPath == null) {
//				throw new BizException("没有指定log目录，请在-Dlog.dir指定或者coreContext.xml里指定logDirPath属性");
//			}
//
//			Level curLogLevel = logLevelMap.get(logLevel);
//
//			Logger rootLogger = Logger.getRootLogger();
//
//			RollingFileAppender appender = (RollingFileAppender) rootLogger.getAppender("rootLoggerAppender");
//
//			if (appender == null) {
//
//				PatternLayout patternLayout = new PatternLayout();
//				File fileDir = new File(logDirPath);
//				if (!fileDir.exists()) {
//					fileDir.mkdirs();
//				}
//
//				if (!logDirPath.endsWith("/")) {
//					logDirPath = logDirPath + "/";
//				}
//				String rootLoggerName = logDirPath + this.rootLoggerName;
//
//				appender = new RollingFileAppender(patternLayout, rootLoggerName);
//				appender.setEncoding("gbk");
//				appender.setMaxFileSize(maxFileSize);
//				appender.setMaxBackupIndex(maxBackupIndex);
//				appender.setName("rootLoggerAppender");
//
//				rootLogger.addAppender(appender);
//
//			}
//
//			if (curLogLevel == null) {
//				Logger.getRootLogger().setLevel(Level.DEBUG);
//				System.out.println("没有设置日志level，使用默认的日志debug级别");
//			} else {
//				Logger.getRootLogger().setLevel(curLogLevel);
//				System.out.println("使用日志" + logLevel + "级别");
//			}
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		// }
//
//	}
//
//	/**
//	 * @return the systemName
//	 */
//	public String getSystemName() {
//		return systemName;
//	}
//
//	/**
//	 * @param systemName
//	 *            the systemName to set
//	 */
//	public void setSystemName(String systemName) {
//		this.systemName = systemName;
//	}
//
//	/**
//	 * @return the maxBackupIndex
//	 */
//	public int getMaxBackupIndex() {
//		return maxBackupIndex;
//	}
//
//	/**
//	 * @param maxBackupIndex
//	 *            the maxBackupIndex to set
//	 */
//	public void setMaxBackupIndex(int maxBackupIndex) {
//		this.maxBackupIndex = maxBackupIndex;
//	}
//
//	/**
//	 * @return the maxFileSize
//	 */
//	public String getMaxFileSize() {
//		return maxFileSize;
//	}
//
//	/**
//	 * @param maxFileSize
//	 *            the maxFileSize to set
//	 */
//	public void setMaxFileSize(String maxFileSize) {
//		this.maxFileSize = maxFileSize;
//	}
//
//	/**
//	 * @return the logServerPort
//	 */
//	public int getLogServerPort() {
//		return logServerPort;
//	}
//
//	/**
//	 * @param logServerPort
//	 *            the logServerPort to set
//	 */
//	public void setLogServerPort(int logServerPort) {
//		this.logServerPort = logServerPort;
//	}
//
//	/**
//	 * @return the moduleInPackageIndex
//	 */
//	public int getModuleInPackageIndex() {
//		return moduleInPackageIndex;
//	}
//
//	/**
//	 * @param moduleInPackageIndex
//	 *            the moduleInPackageIndex to set
//	 */
//	public void setModuleInPackageIndex(int moduleInPackageIndex) {
//		this.moduleInPackageIndex = moduleInPackageIndex;
//	}
//
//	/**
//	 * ��ʼ��ҵ��loggerģ��
//	 *
//	 * @param ����˵�� ��ÿ������һ�У�ע����ȡֵ��Χ��
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣 ��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//
//	private RollingFileAppender initAppender(Logger logger, String busiModule, String logName) {
//		try {
//
//			// ��tomcat�Ļ����������ȡ��Ӫlog��Ŀ¼
//			String logDir = System.getProperty("log.dir");
//
//			// tomcat��û�У���get/set�������ȡ
//			if (logDir == null || logDir.trim().length() <= 0) {
//				logDir = logDirPath;
//			}
//
//			if (logDir == null || logDir.trim().length() <= 0) {
//				// fileDirPath = "D:/log/newlog";
//				throw new RuntimeException("û������log��Ŀ¼");
//			}
//
//			if (!logDir.endsWith("//")) {
//				logDir = logDir + "//";
//			}
//
//			String bizLogDirPath = logDir + busiModule + "//";
//			if (logName.equals(MONITORLOG) && monitorRemoteLogOpened) {
//				bizLogDirPath = logDir + "monitor" + "//" + busiModule + "//";
//			}
//
//			Date curDate = new Date(); // ��ǰ������yyyyMMddHHmm
//			SimpleDateFormat dateFormat = new SimpleDateFormat();
//			dateFormat.applyPattern("yyyyMMdd");
//			StringBuffer dateDirStrBuf = new StringBuffer();
//			dateDirStrBuf.append(bizLogDirPath);
//			dateDirStrBuf.append("/");
//			dateDirStrBuf.append(dateFormat.format(curDate));
//			dateDirStrBuf.append("/");
//
//			StringBuffer logNameStrBuf = new StringBuffer();
//			logNameStrBuf.append(dateDirStrBuf);
//			logNameStrBuf.append(logName).toString();
//
//			String loggerName = busiModule + "_" + logName;
//
//			RollingFileAppender fileAppender = (RollingFileAppender) logger.getAppender(loggerName);
//
//			if (fileAppender == null) {
//				synchronized (Log4JLogger.class) {
//					fileAppender = (RollingFileAppender) logger.getAppender(loggerName);
//					if (fileAppender == null) {
//						PatternLayout patternLayout = new PatternLayout();
//						if (logName.equals(MONITORLOG) && monitorRemoteLogOpened) {
//							patternLayout.setConversionPattern("%m%n");
//						}
//						File fileDir = new File(dateDirStrBuf.toString());
//						if (!fileDir.exists()) {
//							fileDir.mkdirs();
//						}
//
//						fileAppender = new RollingFileAppender(patternLayout, logNameStrBuf.toString());
//						fileAppender.setEncoding("gbk");
//						fileAppender.setMaxFileSize(maxFileSize);
//						fileAppender.setMaxBackupIndex(maxBackupIndex);
//						fileAppender.setName(loggerName);
//						logger.addAppender(fileAppender);
//
//						if (logName.equals(MONITORLOG) && monitorRemoteLogOpened) {
//							// 是否开启远程sql操作
//							// ���logServerIp��Ϊ�գ�����Ҫ���socketAppender
//							if (logServerIp != null && logServerPort != 0) {
//
//								socketAppender.setLocationInfo(true);
//								// ��������ӿڵ���ʽ
//								socketAppender.setLayout(patternLayout);
//
//								logger.addAppender(socketAppender);
//
//							}
//						}
//					}
//				}
//			} else if (!fileAppender.getFile().equals(logNameStrBuf.toString())) {
//
//				synchronized (Log4JLogger.class) {
//					fileAppender = (RollingFileAppender) logger.getAppender(loggerName);
//					if (!fileAppender.getFile().equals(logNameStrBuf.toString())) {
//
//						File bizLogDir = new File(bizLogDirPath);
//						File[] files = bizLogDir.listFiles();
//						if (files != null && files.length > 1) {
//
//							// ��������ļ���������趨����������ɾ����С����һ��
//							int fileLen = files.length;
//							if (fileLen >= maxBackupDateIndex) {
//
//								File smallDateFile = null;
//								for (File curfile : files) {
//
//									if (smallDateFile == null) {
//										smallDateFile = curfile;
//									} else {
//										String smallDateName = smallDateFile.getName();
//										String curfileName = curfile.getName();
//
//										if (curfileName.compareTo(smallDateName) < 0) {
//											smallDateFile = curfile;
//										}
//
//									}
//
//								}
//
//								if (smallDateFile != null && smallDateFile.exists()) {
//									try {
//										FileUtils.deleteDirectory(smallDateFile);
//									} catch (IOException e) {
//									}
//								}
//							}
//						}
//
//						PatternLayout patternLayout = new PatternLayout();
//						File fileDir = new File(dateDirStrBuf.toString());
//						if (!fileDir.exists()) {
//							fileDir.mkdirs();
//						}
//
//						// ����һ����ļ�·����һ��Ƚϲ�ͬ�ر�������ļ�����ֹfd���
//						fileAppender.close();
//						// ɾ��logger���appender����ֹ�ڴ�й¶
//						logger.removeAppender(fileAppender);
//						fileAppender = new RollingFileAppender(patternLayout, logNameStrBuf.toString());
//						fileAppender.setEncoding("gbk");
//						fileAppender.setMaxFileSize(maxFileSize);
//						fileAppender.setMaxBackupIndex(maxBackupIndex);
//						fileAppender.setName(loggerName);
//						logger.addAppender(fileAppender);
//					}
//				}
//			}
//
//			return fileAppender;
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//
//	public void errorTrace(String message) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void errorTrace(String message, Throwable t) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public String getLogDirPath() {
//		return logDirPath;
//	}
//
//	public void setLogDirPath(String logDirPath) {
//		this.logDirPath = logDirPath;
//	}
//
//	public boolean isSysOutDebug() {
//		return outDebug;
//	}
//
//	public void setOutDebug(boolean outDebug) {
//		this.outDebug = outDebug;
//	}
//
//	public String getLogLevel() {
//		return logLevel;
//	}
//
//	public void setLogLevel(String logLevel) {
//		this.logLevel = logLevel;
//	}
//}