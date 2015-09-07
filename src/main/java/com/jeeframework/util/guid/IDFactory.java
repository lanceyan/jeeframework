package com.jeeframework.util.guid;

/**
 * @author lance
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IDFactory
{
    public IDFactory()
    {
        super();
    }

    public static Long generateID()
    {
        // time id
        String timeId = (String.valueOf(System.currentTimeMillis()));
        // random id
        String randomId = String.valueOf(((int) Math
                .floor(Math.random() * 1000)));
        return new Long(timeId + randomId);
    }

}
