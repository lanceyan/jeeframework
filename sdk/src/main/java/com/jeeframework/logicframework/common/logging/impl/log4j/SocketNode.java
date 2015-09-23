///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.jeeframework.logicframework.common.logging.impl.log4j;
//
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.io.File;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.BufferedInputStream;
//
//import org.apache.log4j.*;
//import org.apache.log4j.spi.*;
//
//import Validate;
//
//// Contributors: Moses Hohman <mmhohman@rainbow.uchicago.edu>
//
///**
// * Read {@link LoggingEvent} objects sent from a remote client using Sockets
// * (TCP). These logging events are logged according to local policy, as if they
// * were generated locally.
// *
// * <p>
// * For example, the socket node might decide to log events to a local file and
// * also resent them to a second socket node.
// *
// * @author Ceki G&uuml;lc&uuml;
// *
// * @since 0.8.4
// */
//public class SocketNode implements Runnable {
//
//	Socket socket;
//
//	LoggerRepository hierarchy;
//
//	ObjectInputStream ois;
//
//	String runLogDir;
//
//	static Logger logger = Logger.getLogger(SocketNode.class);
//
//	// public SocketNode(Socket socket, LoggerRepository hierarchy)
//	// {
//	// this.socket = socket;
//	// this.hierarchy = hierarchy;
//	// try
//	// {
//	// ois = new ObjectInputStream(new
//	// BufferedInputStream(socket.getInputStream()));
//	// } catch (Exception e)
//	// {
//	// logger.error("Could not open ObjectInputStream to " + socket, e);
//	// }
//	// }
//
//	public SocketNode(Socket socket, LoggerRepository hierarchy, String runLogDir) {
//		this.socket = socket;
//		this.hierarchy = hierarchy;
//		this.runLogDir = runLogDir;
//		if (!Validate.isEmpty(this.runLogDir)) {
//			File fileDir = new File(this.runLogDir);
//			if (!fileDir.exists()) {
//				fileDir.mkdirs();
//			}
//		}
//		try {
//			ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//		} catch (Exception e) {
//			logger.error("Could not open ObjectInputStream to " + socket, e);
//		}
//	}
//
//	// public
//	// void finalize() {
//	// System.err.println("-------------------------Finalize called");
//	// System.err.flush();
//	// }
//
//	public void run() {
//		LoggingEvent event;
//		Logger remoteLogger;
//
//		try {
//			if (ois != null) {
//				while (true) {
//					// read an event from the wire
//					event = (LoggingEvent) ois.readObject();
//					// get a logger from the hierarchy. The name of the logger
//					// is taken to be the name contained in the
//					// event.
//
//					remoteLogger = hierarchy.getLogger(event.getLoggerName());
//
//					PatternLayout patternLayout = new PatternLayout();
//
//					patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss}%m%n");
//
//					String fileDirPath = this.runLogDir;
//
//					if (fileDirPath == null || fileDirPath.length() <= 0) {
//						// fileDirPath = "D:/log/newlog";
//						throw new RuntimeException("û��������Ӫ����log��Ŀ¼");
//					}
//
//					if (!fileDirPath.endsWith("//")) {
//						fileDirPath = fileDirPath + "//";
//					}
//
//					Date curDate = new Date(); // ��ǰ������yyyyMMddHHmm
//					SimpleDateFormat dateFormat = new SimpleDateFormat();
//					dateFormat.applyPattern("yyyyMMdd");
//					StringBuffer dataStrBuf = new StringBuffer();
//					dataStrBuf.append(fileDirPath);
//					if (!(dataStrBuf.toString().lastIndexOf("/") == -1)) {
//						dataStrBuf.append("/");
//					}
//					dataStrBuf.append(dateFormat.format(curDate));
//
//					if (!(dataStrBuf.toString().lastIndexOf("/") == -1)) {
//						dataStrBuf.append("/");
//					}
//
//					// 远程日志是这样 name + TRANSING_LOG_SPLIT + monitor.log
//					String romoteLoggerName = event.getLoggerName();
//					String[] romoteLoggerNameArr = romoteLoggerName.split(Log4JLogger.TRANSING_LOG_SPLIT);
//
//					dataStrBuf.append(romoteLoggerNameArr[0]).toString();
//
//					if (!(dataStrBuf.toString().lastIndexOf("/") == -1)) {
//						dataStrBuf.append("/");
//					}
//
//					File fileDir = new File(dataStrBuf.toString());
//					if (!fileDir.exists()) {
//						fileDir.mkdirs();
//					}
//
//					dataStrBuf.append(romoteLoggerNameArr[1]).toString();
//
//					// dataStrBuf.append( event.getLoggerName()).toString();
//
//					RollingFileAppender fileAppender = (RollingFileAppender) remoteLogger.getAppender(event.getLoggerName());
//
//					if (fileAppender == null) {
//						fileAppender = new RollingFileAppender(patternLayout, dataStrBuf.toString());
//						fileAppender.setEncoding("gbk");
//						fileAppender.setMaxFileSize("10MB");
//						fileAppender.setMaxBackupIndex(300);
//						fileAppender.setName(event.getLoggerName());
//						remoteLogger.addAppender(fileAppender);
//					} else if (!fileAppender.getFile().equals(dataStrBuf.toString())) {
//						// ����һ����ļ�·����һ��Ƚϲ�ͬ�ر�������ļ�����ֹfd���
//						fileAppender.close();
//						// ɾ��logger���appender����ֹ�ڴ�й¶
//						remoteLogger.removeAppender(fileAppender);
//						fileAppender = new RollingFileAppender(patternLayout, dataStrBuf.toString());
//						fileAppender.setEncoding("gbk");
//						fileAppender.setMaxFileSize("10MB");
//						fileAppender.setMaxBackupIndex(300);
//						fileAppender.setName(event.getLoggerName());
//						remoteLogger.addAppender(fileAppender);
//					}
//					// if (remoteLogger.getAppender(event.getLoggerName()) ==
//					// null)
//					// {
//					// RollingFileAppender fileAppender = new
//					// RollingFileAppender(patternLayout, dataStrBuf.append(
//					// event.getLoggerName()).toString());
//					//
//					// fileAppender.setMaxFileSize("10MB");
//					// fileAppender.setMaxBackupIndex(300);
//					// fileAppender.setName(event.getLoggerName());
//					// remoteLogger.addAppender(fileAppender);
//					// }
//
//					// event.logger = remoteLogger;
//					// apply the logger-level filter
//					if (event.getLevel().isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
//						// finally log the event as if was generated locally
//						remoteLogger.callAppenders(event);
//					}
//				}
//			}
//		} catch (java.io.EOFException e) {
//			logger.info("Caught java.io.EOFException closing conneciton.");
//		} catch (java.net.SocketException e) {
//			logger.info("Caught java.net.SocketException closing conneciton.");
//		} catch (IOException e) {
//			logger.info("Caught java.io.IOException: " + e);
//			logger.info("Closing connection.");
//		} catch (Exception e) {
//			logger.error("Unexpected exception. Closing conneciton.", e);
//		} finally {
//			if (ois != null) {
//				try {
//					ois.close();
//				} catch (Exception e) {
//					logger.info("Could not close connection.", e);
//				}
//			}
//			if (socket != null) {
//				try {
//					socket.close();
//				} catch (IOException ex) {
//				}
//			}
//		}
//	}
//}
