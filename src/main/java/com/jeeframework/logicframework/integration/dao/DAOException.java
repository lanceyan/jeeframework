package com.jeeframework.logicframework.integration.dao;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import com.jeeframework.logicframework.integration.DataServiceException;


/** 
 * DAO����쳣
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see com.jeeframework.logicframework.integration.DataServiceException
 */
	
public class DAOException extends DataServiceException
{

    /**
     * ���캯���װ�׳������쳣
     * @param cause
     * 
     */

    public DAOException(Throwable cause)
    {
        super(cause);
    }

    /**
     * ���캯���װ�쳣��Ϣ
     * @param message
     * 
     */

    public DAOException(String message)
    {

        super(message);

    }

    /**
     * ���캯���װ��Ϣ���쳣����
     * @param message
     * @param cause
     * 
     */

    public DAOException(String message, Throwable cause)
    {

        super(message, cause);

    }

    /**
     * ���쳣ת��Ϊstring
     * @return �쳣��string��ʽ
     */
    public String toString()
    {
        if (getCause() instanceof DataAccessException)
        {
            DataAccessException dataAccessException = (DataAccessException) getCause();
            if (dataAccessException.getCause() instanceof SQLException)
            {
                SQLException e = (SQLException) dataAccessException.getCause();
                // return "" + e.getErrorCode();
                return e.toString();
            }
            return super.toString();
        }
        return super.toString();
    }

//    public static void main(String arg[])
//    {
        // SQLException sqlException = new SQLException("hh");
        //        
        // DataAccessException daoe = new
        // CleanupFailureDataAccessException("cleanfail",sqlException);
        // DAOException pdaoe= new DAOException(daoe);
        // try{
        // throw new ApplicationControllerException(daoe);
        // }catch(ApplicationControllerException e){
        // System.out.println(e.getMessage());
        // System.out.println(e.getCause().getMessage());
        // }
 //   }
}
