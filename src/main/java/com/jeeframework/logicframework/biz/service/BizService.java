package com.jeeframework.logicframework.biz.service;

import com.jeeframework.logicframework.integration.error.ErrorInfo;




/** 
  * ���ڱ�ʶҵ����service
  * @author lanceyan�������޸��ߣ�
  * @version 1.0���°汾�ţ�
  * 
*/
	
public interface BizService {

    /**
     * ����ӿڷ������ڻ�ȡSAO��DAO���ص��쳣��Ϣ
     * @return ErrorInfo  ������Ϣ
     */
    public ErrorInfo getLastErrorInfo();
}