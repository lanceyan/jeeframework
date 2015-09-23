package com.jeeframework.logicframework.integration.sao;


import com.jeeframework.logicframework.integration.DataServiceException;




/** 
 * SAO����쳣��Ϣ
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see com.jeeframework.logicframework.integration.DataServiceException
 */
	
public class SAOException extends DataServiceException {




    public SAOException(Throwable cause) {
        super(cause);
    }




    public SAOException(String message) {
        super(message);
    }




    public SAOException(String message, Throwable cause) {
        super(message, cause);
    }



}

