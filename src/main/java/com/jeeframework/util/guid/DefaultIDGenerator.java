package com.jeeframework.util.guid;

/**
 * @author lance
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefaultIDGenerator
{
	public static String generateID()
	{
		StringBuffer sb = new StringBuffer();
		// current time stamp
		Long id = Long.valueOf(System.currentTimeMillis());
		sb.append(id.longValue());
		// random id
		Double randomId = new Double(Math.floor(Math.random() * 1000));
		sb.append(randomId.intValue());
		return sb.toString();
	}
}
