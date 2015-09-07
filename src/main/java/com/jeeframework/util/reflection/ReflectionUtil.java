/**
 * Copyright (C) 1998-2007 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��ReflectionUtil.java					
 *			
 * Description����Ҫ�������ļ�������							 												
 * History��
 * �汾��    ����      ����       ��Ҫ������ز���
 *  1.0   lanceyan  2008-6-16  Create	
 */

package com.jeeframework.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class ReflectionUtil
{

    /**
     * Retrieves the value of the specified boolean-field of the given object.
     * 
     * @param object
     *            the object that holds the field
     * @param fieldName
     *            the name of the field
     * @return the field value
     * @throws NoSuchFieldException
     *             when the field does not exist
     */
    public static boolean getBooleanField(Object object, String fieldName) throws NoSuchFieldException
    {

        Field field = getField(object, fieldName);
        try
        {
            return field.getBoolean(object);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("unable to access field [" + fieldName + "]: " + e.toString());
        }
    }

    /**
     * Retrieves the value of the specified String-field of the given object.
     * 
     * @param object
     *            the object that holds the field
     * @param fieldName
     *            the name of the field
     * @return the field value
     * @throws NoSuchFieldException
     *             when the field does not exist
     */
    public static String getStringField(Object object, String fieldName) throws NoSuchFieldException
    {
        Field field = getField(object, fieldName);
        try
        {
            return (String) field.get(object);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("unable to access field [" + fieldName + "]: " + e.toString());
        }
    }

    /**
     * Retrieves the specified field of the given object.
     * 
     * @param object
     *            the object that holds the field
     * @param fieldName
     *            the name of the field
     * @return the field
     * @throws NoSuchFieldException
     *             when the field does not exist
     */
    public static Field getField(Object object, String fieldName) throws NoSuchFieldException
    {
        return getField(object.getClass(), fieldName);
    }

    /**
     * Retrieves the specified field of the given object.
     * 
     * @param instanceClass
     *            the class that contains the field
     * @param fieldName
     *            the name of the field
     * @return the field
     * @throws NoSuchFieldException
     *             when the field does not exist
     */
    public static Field getField(Class instanceClass, String fieldName) throws NoSuchFieldException
    {
        try
        {
            Field field = null;
            while (field == null)
            {
                try
                {
                    field = instanceClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e)
                {
                    instanceClass = instanceClass.getSuperclass();
                    if (instanceClass == null)
                    {
                        throw e;
                    }
                }
            }
            field.setAccessible(true);
            return field;
        } catch (SecurityException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to access field [" + fieldName + "]: " + e.toString());
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to access field [" + fieldName + "]: " + e.toString());
        }
    }

    /**
     * Sets a field value for the given object.
     * 
     * @param object
     *            the object that should be changed
     * @param fieldName
     *            the name of the field
     * @param value
     *            the value
     * @throws NoSuchFieldException
     *             when the field does not exist or could not be written
     */
    public static void setField(Object object, String fieldName, int value) throws NoSuchFieldException
    {
        setField(object, fieldName, Integer.valueOf(value));
    }

    /**
     * Sets a field value for the given object.
     * 
     * @param object
     *            the object that should be changed
     * @param fieldName
     *            the name of the field
     * @param value
     *            the value
     * @throws NoSuchFieldException
     *             when the field does not exist or could not be written
     */
    public static void setField(Object object, String fieldName, Object value) throws NoSuchFieldException
    {
        try
        {
            Field field = null;
            Class instanceClass = object.getClass();
            while (field == null)
            {
                try
                {
                    field = instanceClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e)
                {
                    instanceClass = instanceClass.getSuperclass();
                    if (instanceClass == null)
                    {
                        throw e;
                    }
                }
            }
            field.setAccessible(true);
            field.set(object, value);
        } catch (SecurityException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        }
    }

    /**
     * Sets a field value for the given object.
     * 
     * @param fieldClass
     *            the class that should be changed
     * @param fieldName
     *            the name of the field
     * @param value
     *            the value
     * @throws NoSuchFieldException
     *             when the field does not exist or could not be written
     */
    public static void setStaticField(Class fieldClass, String fieldName, Object value) throws NoSuchFieldException
    {
        try
        {
            Field field = null;
            while (field == null)
            {
                try
                {
                    field = fieldClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e)
                {
                    fieldClass = fieldClass.getSuperclass();
                    if (fieldClass == null)
                    {
                        throw e;
                    }
                }
            }
            field.setAccessible(true);
            field.set(null, value);
        } catch (SecurityException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new NoSuchFieldException("Unable to set field [" + fieldName + "]: " + e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object callMethod(Object object, String methodName, int value) throws NoSuchMethodException
    {
        Class instanceClass = object.getClass();
        Method method = null;
        while (method == null)
        {
            try
            {
                method = instanceClass.getDeclaredMethod(methodName, new Class[] { Integer.TYPE });
            } catch (NoSuchMethodException e)
            {
                instanceClass = instanceClass.getSuperclass();
                if (instanceClass == null)
                {
                    throw e;
                }
            }
        }
        method.setAccessible(true);
        try
        {
            return method.invoke(object, new Object[] { Integer.valueOf(value) });
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new NoSuchMethodException(e.toString());
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object callMethod(String methodName, Object object, Class[] signature, Object[] values)
            throws NoSuchMethodException
    {
        Class instanceClass = object.getClass();
        Method method = null;
        while (method == null)
        {
            try
            {
                method = instanceClass.getDeclaredMethod(methodName, signature);
            } catch (NoSuchMethodException e)
            {
                instanceClass = instanceClass.getSuperclass();
                if (instanceClass == null)
                {
                    throw e;
                }
            }
        }
        method.setAccessible(true);
        try
        {
            return method.invoke(object, values);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new NoSuchMethodException(e.toString());
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object callMethod(String methodName, Object object) throws NoSuchMethodException
    {
        Class instanceClass = object.getClass();
        Method method = null;
        while (method == null)
        {
            try
            {
                method = instanceClass.getDeclaredMethod(methodName, new Class[0]);
            } catch (NoSuchMethodException e)
            {
                instanceClass = instanceClass.getSuperclass();
                if (instanceClass == null)
                {
                    throw e;
                }
            }
        }
        method.setAccessible(true);
        try
        {
            return method.invoke(object, new Object[0]);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new NoSuchMethodException(e.toString());
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * @param object
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException
    {
        Field field = getField(object, fieldName);
        return field.get(object);
    }

    /**
     * @param parameterClass
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     */
    public static Object getStaticFieldValue(Class parameterClass, String fieldName) throws IllegalArgumentException,
            IllegalAccessException, NoSuchFieldException
    {
        Field field = getField(parameterClass, fieldName);
        return field.get(null);
    }

}
