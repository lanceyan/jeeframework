package com.jeeframework.logicframework.integration.bo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

/**
 * ����ģ�ͣ����ڱ�ӳ���BO�ࡣ ����BO����̳�AbstractBO��
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 */

public abstract class AbstractBO implements DomainModule {
    /**
     * ��ǰbean��������
     */
    protected BeanFactory context = null;

    /**
     * �õ����õ�BO bean������
     */

    /**
     * BO��Ĭ�Ϲ��캯��
     */
    public AbstractBO() {
        super();
    }

    /**
     * ��ʼ��bean��context
     * 
     * @param beanFactory �����Զ���bean�ĳ�ʼ��������
     * @exception org.springframework.beans.BeansException
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }

    /**
     * spring����ٷ��� ���������Լ������Զ���
     * 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() {
    }

    /**
     * spring�ĳ�ʼ�������û�������д����ʼ������
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * ���������������tostring����
     * 
     * @param ����˵����ÿ������һ�У�ע����ȡֵ��Χ��
     * @return ����ֵ��ע�ͳ�ʧ�ܡ������쳣ʱ�ķ������
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer res = new StringBuffer(getClass().getName() + " \n");
        Method[] methods = getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method tmp = methods[i];
            String mName = tmp.getName();
            if (mName.startsWith("get")) {
                String fName = mName.substring(3);
                res.append(fName + " = ");
                try {
                    Object o = tmp.invoke(this, (Object[]) null);
                    if (o == null) {
                        res.append(" null \n");
                    } else {
                        res.append("" + o.toString() + " \n");
                    }
                } catch (IllegalAccessException e) {
                    continue;
                } catch (InvocationTargetException e) {
                    continue;
                }
            }
        }
        return res.toString();
    }

}