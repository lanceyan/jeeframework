/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��DataService.java					
 *			
 * Description�����ɲ����ݷ���ӿ�						 * 												 * History��
* �汾��    ����      ����       ��Ҫ������ز���
*  1.0   lanceyan  2008-5-15  Create	
*/

package com.jeeframework.logicframework.integration;

import com.jeeframework.logicframework.integration.error.ErrorInfo;



/** 
 * ���ɲ����ݷ��񹫹��ӿ�
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public interface DataService
{
    /**
     * ��ȡ��ǰ�������Ĵ�����Ϣ
     * @return ErrorInfo ��������Ϣ
     */
    public  ErrorInfo getLastErrorInfo();
}


