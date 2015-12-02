package com.jeeframework.logicframework.integration.dao.datasource;

/**
 * ���db��������� db url ����Ϣ��������
 * 
 * @author lanceyan
 */
public class JdbcInfoHelper
{
	/**
	 * @param dbType ��ݿ�����
	 * @param ip	ip��ַ
	 * @param port	�˿�
	 * @param dbname	������
	 * @param usr �û���
	 * @param pwd ����
	 * @return
	 */
	public static DbcpInfo getDBCPInfo(String dbType, String ip, String port, String dbname, String usr, String pwd)
	{
		DbcpInfo ret = new DbcpInfo();

		if (dbType.equalsIgnoreCase("ORACLE"))
		{
			ret.url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + "dbname";
			ret.driverClassName = "oracle.jdbc.driver.OracleDriver";
			ret.username = usr;
			ret.password = pwd;
			ret.driverClassName = "select 1 from dual";
			ret.testOnBorrow = "true";
		}
		else if (dbType.equalsIgnoreCase("mysql"))
		{
			ret.url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
			ret.driverClassName = "com.mysql.jdbc.Driver";
			ret.username = usr;
			ret.password = pwd;
			ret.driverClassName = "select 1 from dual";
			ret.testOnBorrow = "true";
		}

		return ret;
	}

}
