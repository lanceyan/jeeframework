///**
// * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
// *
// * FileName��PerfMapUtil.java
// *
// * Description������ͳ�Ƶ�map������						 * 												 * History��
// * �汾��    ����      ����       ��Ҫ������ز���
// *  1.0   lanceyan  2008-5-16  Create
// */
//
//package com.jeeframework.logicframework.util.perstat;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import Logger;
//import LoggerUtil;
//
///**
// * ����
// *
// * @author lanceyan�������޸��ߣ�
// * @version 1.0���°汾�ţ�
// *
// */
//
//public class PerfMapUtil {
//	// objectPerfMap<����Map<������List[0-10ms,10-20ms,20-30ms,30-40ms,40-50ms,50ms++]>>
//	private static Map<String, Map<String, List<Long>>> objectPerfMap = new HashMap<String, Map<String, List<Long>>>();
//
//	private final static String OBJ_COUNT = "objCount"; // ��������
//
//	protected static final Logger logger = LoggerUtil.getLogger();
//
//	/**
//	 * ����
//	 *
//	 * @param className
//	 *            ��Ҫ��¼������
//	 * @param methodName
//	 *            ��Ҫ��¼������
//	 * @param objCount
//	 *            ��ǰ���еĶ������
//	 * @param executeTime
//	 *            ִ��ʱ��
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public static void addPerfData(String className, String methodName,
//			long objCount, long executeTime) {
//		if (null != objectPerfMap) {
//			synchronized (objectPerfMap) {
//				Map<String, List<Long>> methodPerfMap = null;
//				List<Long> innerPerfList = null;
//				if (objectPerfMap.get(className) == null) {
//					methodPerfMap = new HashMap<String, List<Long>>();
//					objectPerfMap.put(className, methodPerfMap);
//				} else {
//					methodPerfMap = objectPerfMap.get(className);
//				}
//
//				if (methodPerfMap.get(methodName) == null) {
//					innerPerfList = new ArrayList<Long>(6);
//					innerPerfList.add(Long.valueOf(0));// 0-10ms
//					innerPerfList.add(Long.valueOf(0));// 10-20ms
//					innerPerfList.add(Long.valueOf(0));// 20-30ms
//					innerPerfList.add(Long.valueOf(0));// 30-40ms
//					innerPerfList.add(Long.valueOf(0));// 40-50ms
//					innerPerfList.add(Long.valueOf(0));// 50ms++
//					setArrayTime(innerPerfList, executeTime);
//					methodPerfMap.put(methodName, innerPerfList);
//
//					innerPerfList = new ArrayList<Long>(1);
//					innerPerfList.add(Long.valueOf(objCount));
//					methodPerfMap.put(OBJ_COUNT, innerPerfList); // �������
//				} else {
//					innerPerfList = methodPerfMap.get(methodName);
//					innerPerfList.set(0, Long.valueOf(objCount));
//					setArrayTime(innerPerfList, executeTime);
//				}
//			}
//		}
//	}
//
//	/**
//	 * ���ڴ��е�������ݴ洢��������־��
//	 *
//	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	public static void dumpPerfData() {
//		if (null != objectPerfMap) {
//			synchronized (objectPerfMap) {
//				Iterator<String> objPerfKeyIter = objectPerfMap.keySet()
//						.iterator();
//				Iterator<String> methodPerfKeyIter = null; // �������ܼ��ϵ��
//				Map<String, List<Long>> methodPerfMap = null; // �������ܼ���Map
//				List<Long> innerPerfList = null; // ͳ�ƵĶ������ֵ
//				String objClassName = null; // ͳ�Ƶ�����
//				String objMethodName = null; // ͳ�Ƶķ�����
//				StringBuffer logMessagePC = null; // ��������
//				StringBuffer logMessagePR = null; // �������ϱ�ֵ
//				while (objPerfKeyIter.hasNext()) {
//					objClassName = objPerfKeyIter.next();
//					methodPerfMap = objectPerfMap.get(objClassName);
//					methodPerfKeyIter = methodPerfMap.keySet().iterator();
//					while (methodPerfKeyIter.hasNext()) {
//						objMethodName = methodPerfKeyIter.next();
//						innerPerfList = methodPerfMap.get(objMethodName);
//						logMessagePC = new StringBuffer();
//						if (objMethodName.equals(OBJ_COUNT)) {
//							logMessagePC.append(objClassName);
//							logMessagePR = new StringBuffer();
//							logMessagePR.append("c:");
//							logMessagePR.append(innerPerfList.get(0));
//							logger.perTrace(logMessagePC.toString(),
//									logMessagePR.toString());
//							continue;
//						}
//
//						logMessagePC.append(objClassName);
//						logMessagePC.append(".");
//						logMessagePC.append(objMethodName);
//						// (0-10ms]:5,(10-20ms]:1,(20-30ms]:0,(30-40ms]:0,(40-50ms]:0,(50ms,..):0
//						logMessagePR = new StringBuffer();
//						logMessagePR.append("(");
//						logMessagePR.append(PerLogTask.range1
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range1
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(0));
//						logMessagePR.append(",(");
//						logMessagePR.append(PerLogTask.range2
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range2
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(1));
//						logMessagePR.append(",(");
//						logMessagePR.append(PerLogTask.range3
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range3
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(2));
//						logMessagePR.append(",(");
//						logMessagePR.append(PerLogTask.range4
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range4
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(3));
//						logMessagePR.append(",(");
//						logMessagePR.append(PerLogTask.range5
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range5
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(4));
//						logMessagePR.append(",(");
//						logMessagePR.append(PerLogTask.range6
//								.getProperty("lowerLimit"));
//						logMessagePR.append("-");
//						logMessagePR.append(PerLogTask.range6
//								.getProperty("upperLimit"));
//						logMessagePR.append("ms]:");
//						logMessagePR.append(innerPerfList.get(5));
//						logger.perTrace(logMessagePC.toString(), logMessagePR
//								.toString());
//					}
//				}
//				objectPerfMap.clear();
//			}
//		}
//	}
//
//	/**
//	 * ͨ��ִ��ʱ�����õ�ÿ����ķ�������
//	 *
//	 * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
//	 * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
//	 * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
//	 *
//	 */
//	private static void setArrayTime(List<Long> innerPerfList, long executeTime) {
//		if (PerLogTask.range1 == null) {
//			PerLogTask.initProp();
//		}
//		long range1LowerLimit = Long.valueOf(PerLogTask.range1
//				.getProperty("lowerLimit"));
//		long range1UpperLimit = Long.valueOf(PerLogTask.range1
//				.getProperty("upperLimit"));
//		long range2LowerLimit = Long.valueOf(PerLogTask.range2
//				.getProperty("lowerLimit"));
//		long range2UpperLimit = Long.valueOf(PerLogTask.range2
//				.getProperty("upperLimit"));
//		long range3LowerLimit = Long.valueOf(PerLogTask.range3
//				.getProperty("lowerLimit"));
//		long range3UpperLimit = Long.valueOf(PerLogTask.range3
//				.getProperty("upperLimit"));
//		long range4LowerLimit = Long.valueOf(PerLogTask.range4
//				.getProperty("lowerLimit"));
//		long range4UpperLimit = Long.valueOf(PerLogTask.range4
//				.getProperty("upperLimit"));
//		long range5LowerLimit = Long.valueOf(PerLogTask.range5
//				.getProperty("lowerLimit"));
//		long range5UpperLimit = Long.valueOf(PerLogTask.range5
//				.getProperty("upperLimit"));
//		long range6LowerLimit = Long.valueOf(PerLogTask.range6
//				.getProperty("lowerLimit"));
//		String range6UpperLimit = PerLogTask.range6.getProperty("upperLimit");
//
//		if (executeTime >= range1LowerLimit && executeTime <= range1UpperLimit) {
//			innerPerfList.set(0, innerPerfList.get(0) + 1);
//		} else if (executeTime > range2LowerLimit
//				&& executeTime <= range2UpperLimit) {
//			innerPerfList.set(1, innerPerfList.get(1) + 1);
//		} else if (executeTime > range3LowerLimit
//				&& executeTime <= range3UpperLimit) {
//			innerPerfList.set(2, innerPerfList.get(2) + 1);
//		} else if (executeTime > range4LowerLimit
//				&& executeTime <= range4UpperLimit) {
//			innerPerfList.set(3, innerPerfList.get(3) + 1);
//		} else if (executeTime > range5LowerLimit
//				&& executeTime <= range5UpperLimit) {
//			innerPerfList.set(4, innerPerfList.get(4) + 1);
//		} else {
//			if (range6UpperLimit.equalsIgnoreCase("MAX")) {
//				innerPerfList.set(5, innerPerfList.get(5) + 1);
//			} else if (executeTime > range6LowerLimit
//					&& executeTime <= Long.valueOf(range6UpperLimit)) {
//				innerPerfList.set(5, innerPerfList.get(5) + 1);
//			}
//		}
//	}
//
//	public static void main(String args[]) {
//		addPerfData("test1", "method1", 1, 1);
//		addPerfData("test2", "method2", 2, 2);
//		addPerfData("test3", "method3", 3, 3);
//		addPerfData("test4", "method4", 4, 4);
//		addPerfData("test5", "method5", 5, 5);
//		dumpPerfData();
//	}
//}
