/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��TimerJob.java
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-9-25           Create	
 */

package com.jeeframework.logicframework.util.timetask;

/**
 * timer�Ľӿ���
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public interface TimerJob
{

    /**
     * ��ʱ������õķ�����ʵ����ʵ���������
     * @exception �׳��ܵ��쳣��
     * 
     */
    public void onTimer()   throws Exception;
}
