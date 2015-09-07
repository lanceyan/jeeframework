/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��CommonTimerTask.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-9-25           Create	
 */

package com.jeeframework.logicframework.common.timetask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ���ö�ʱ��������
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public class CommonTimerTask extends TimerTask implements InitializingBean
{

    private Log logger = LogFactory.getLog(CommonTimerTask.class);

    private long delayTime = 0; // �����ʼ���ӳٵ�ʱ��

    private long periodTime = 0; // ����ִ�м����ʱ��

    private TimerJob timer = null;

    public TimerJob getTimer() {
        return timer;
    }

    public void setTimer(TimerJob timer) {
        this.timer = timer;
    }

    /**
     * �̵߳�run������ͨ����������Ķ�ʱ�������ʵ��
     */
    @Override
    public void run()
    {
        try
        {
            timer.onTimer();
        } catch (Exception e)
        {
            logger.error("��ʱ��������쳣��ϵͳ��¼�쳣��־�� ", e);
        }
    }
    /**
     * �õ���ʱ���񴥷����ӳ�ʱ��
     * @return delaytime  ��ʱ���񴥷����ӳ�ʱ��
     */
    public long getDelayTime()
    {
        return delayTime;
    }

    /**
     * ���ö�ʱ���񴥷����ӳ�ʱ��
     */
    public void setDelayTime(long delayTime)
    {
        this.delayTime = delayTime;
    }
    /**
     * ���ö�ʱ���񴥷��ļ��ʱ��
     * @return PeriodTime  ��ʱ���񴥷��ļ��ʱ��
     */
    public long getPeriodTime()
    {
        return periodTime;
    }
    /**
     * ���ö�ʱ����ִ�еļ��ʱ��
     */
    public void setPeriodTime(long periodTime)
    {
        this.periodTime = periodTime;
    }

    /**
     * ��ĳ�ʼ��������ڳ�ʼ����ʱ��
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception
    {
        Timer timer = new Timer();

        timer.schedule(this, delayTime * 1000, periodTime * 1000);
    }

}
