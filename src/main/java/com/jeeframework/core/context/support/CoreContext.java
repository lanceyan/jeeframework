package com.jeeframework.core.context.support;

import java.sql.Timestamp;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 */
	
public class CoreContext
{

    // use new context file name
    /**
     */
    private static final String CORE_CONTEXT_XML = "core-context.xml";

    /**
     */
    private static ClassPathXmlApplicationContext instance;

    /**
     */
    private CoreContext()
    { // throws BeansException {

        // super(CORE_CONTEXT_XMLFILE_NAME);
    }

    /**
     */
    public static void init()
    {
        getInstance();
    }

    /**
     */
    public static ClassPathXmlApplicationContext getInstance() throws FatalBeanException
    {
        if (instance == null)
        {
            synchronized (CoreContext.class)
            {
                System.out.println("***************************************************************");

                System.out.println("***************************************************************");
                try
                {
                    System.out.println(new Timestamp(System.currentTimeMillis()) + "  <INFO>开始加载"
                            + CORE_CONTEXT_XML + " ....");
                    // load CORE_CONTEXT_XML first
                    instance = new ClassPathXmlApplicationContext(CORE_CONTEXT_XML);
                    System.out.println(new Timestamp(System.currentTimeMillis()) + "  <INFO>成功加载"
                            + CORE_CONTEXT_XML + " .");
                } catch (BeansException beanEx)
                {

                    System.out.println(new Timestamp(System.currentTimeMillis()) + "  <WARNING>错误加载"
                            + CORE_CONTEXT_XML + "!");
                    beanEx.printStackTrace();
                    throw beanEx;
                }

            }
        }

        return instance;
    }
}
