/**
 * Copyright (C) 2015-2015 JEEFRAMEWORK Inc.All Rights Reserved.
 * 																	
 * FileName��XmlUtil.java					
 *			
 * Description��xml�����Ӧ����						 												
 * History��
 * �汾��    ����           ����          ��Ҫ������ز���
 *  1.0   lanceyan        2008-6-27           Create	
 */

package com.jeeframework.util.xml;

import com.jeeframework.util.io.FileUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * xml�����Ӧ����
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class XmlUtil
{

    /**
     * ���xml�ڵ��ȡ����ڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            String ���ڵ�����
     * @return ����ֵ��List<Node>���ӽڵ��list
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static List<Node> getChildElementsByTagName(Element ele, String childEleName)
    {
        NodeList nl = ele.getChildNodes();
        List<Node> childEles = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++)
        {
            Node node = nl.item(i);
            if (node instanceof Element && childEleName.equals(node.getNodeName())
                    || childEleName.equals(node.getLocalName()))
            {
                childEles.add(node);
            }
        }
        return childEles;
    }

    /**
     * ���xml�ڵ��ȡ����ڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            String ���ڵ�����
     * @return ����ֵ��Element �ӽڵ�
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static Element getChildElementByTagName(Element ele, String childEleName)
    {

        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
        {
            Node node = nl.item(i);
            if (node instanceof Element && childEleName.equals(node.getNodeName())
                    || childEleName.equals(node.getLocalName()))
            {
                return (Element) node;
            }
        }
        return null;
    }

    /**
     * ���xml�ڵ��ȡ����ڵ�ֵ
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            String ���ڵ�����
     * @return ����ֵ������ڵ��ֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static String getChildElementValueByTagName(Element ele, String childEleName)
    {

        Element child = getChildElementByTagName(ele, childEleName);
        return (child != null ? getTextValue(child) : null);
    }

    /**
     * ���xml�ڵ��ȡ�ڵ�ֵ
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @return ����ֵ���ڵ��ֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static String getTextValue(Element valueEle)
    {

        StringBuffer value = new StringBuffer();
        NodeList nl = valueEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
        {
            Node item = nl.item(i);
            if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference)
            {
                value.append(item.getNodeValue());
            }
        }
        return value.toString().trim();
    }

    /**
     * ��ݶ�����ַ����һ��xml��document����
     * 
     * @param ����˵��
     *            String ��һ��xml��ʽ���ַ�
     * @return ����ֵ��Document xml��domԪ��
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static Document createDocument(String inputString) throws RuntimeException
    {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        InputStream input;
        try
        {
            DocumentBuilder p = f.newDocumentBuilder();
            input = new ByteArrayInputStream(inputString.getBytes());
            return p.parse(input);
        } catch (ParserConfigurationException e)
        {
            throw new RuntimeException(e);
        } catch (SAXException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * ��ݶ�����ļ����һ��xml��document����
     * 
     * @param ����˵��
     *            inputFile ��һ��xml��ʽ���ļ�
     * @return ����ֵ��Document xml��domԪ��
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static Document createDocument(File inputFile) throws RuntimeException
    {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        InputStream input;
        try
        {
            byte[] fileContent = FileUtils.readFileToByteArray(inputFile);

            DocumentBuilder p = f.newDocumentBuilder();

            input = new ByteArrayInputStream(fileContent);
            return p.parse(input);
        } catch (ParserConfigurationException e)
        {
            throw new RuntimeException(e);
        } catch (SAXException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * ���xml�ڵ㣬���ڵ����֡��ӽڵ����ֻ�ȡ����ڵ�
     * 
     * @param ����˵��
     *            Document ��xml��һ���ڵ�
     * @param ����˵��
     *            parentName �����ڵ�����
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ���ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static String getElementValueByTagName(Document doc, String parentName, String eleName)
    {

        NodeList nl = doc.getElementsByTagName(parentName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        return getChildElementValueByTagName((Element) item, eleName);
    }

    /**
     * ���xml�ڵ㣬�ڵ�����ȡ���ڵ�
     * 
     * @param ����˵��
     *            Document ��xml��һ���ڵ�
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ���ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static Element getElementByTagName(Document doc, String eleName)
    {

        NodeList nl = doc.getElementsByTagName(eleName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        return (Element) item;
    }    
    
    /**
     * ���xml�ڵ㣬���ڵ����֡��ӽڵ����ֻ�ȡ����ڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            parentName �����ڵ�����
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ���ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static String getGrandSonElementValueByTagName(Element element, String parentName, String eleName)
    {

        NodeList nl = element.getElementsByTagName(parentName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        return getChildElementValueByTagName((Element) item, eleName);
    }

    /**
     * ���xml�ڵ㣬���ڵ����֡��ӽڵ����ֻ�ȡ���ӽڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            parentName �����ڵ�����
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ���ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static Element getGrandSonElementByTagName(Element element, String parentName, String eleName)
    {

        NodeList nl = element.getElementsByTagName(parentName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        return getChildElementByTagName((Element) item, eleName);
    }

    /**
     * ���xml�ڵ㣬���ڵ����֡��ӽڵ����ֻ�ȡ���ӽڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            parentName �����ڵ�����
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ��List<String>�ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static List<String> getGrandSonListValueByTagName(Element element, String parentName, String eleName)
    {

        NodeList nl = element.getElementsByTagName(parentName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        if (null == item)
        {
            return null;
        }
        NodeList subNodeList = item.getChildNodes();
        List<String> childEles = new ArrayList<String>();
        Node node = null;
        for (int i = 0; i < subNodeList.getLength(); i++)
        {
            node = subNodeList.item(i);

            if (node != null)
            {
                if (node instanceof Element && eleName.equals(node.getNodeName())
                        || eleName.equals(node.getLocalName()))
                {
                    childEles.add(getTextValue((Element) node));
                }
            }
        }

        return childEles;
    }

    /**
     * ���xml�ڵ㣬���ڵ����֡��ӽڵ����ֻ�ȡ���ӽڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            parentName �����ڵ�����
     * @param ����˵��
     *            eleName ���ӽڵ�����
     * @return ����ֵ��List<Node>�ڵ��Ӧ��valueֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static List<Node> getGrandSonElementsByTagName(Element ele, String parentName, String eleName)
    {

        NodeList nl = ele.getElementsByTagName(parentName);
        if (null == nl)
        {
            return null;
        }
        Node item = nl.item(0);
        if (null == item)
        {
            return null;
        }
        NodeList subNodeList = item.getChildNodes();
        List<Node> childEles = new ArrayList<Node>();
        Node node = null;
        for (int i = 0; i < subNodeList.getLength(); i++)
        {
            node = subNodeList.item(i);

            if (node != null)
            {
                if (node instanceof Element && eleName.equals(node.getNodeName())
                        || eleName.equals(node.getLocalName()))
                {
                    childEles.add(node);
                }
            }
        }

        return childEles;
    }

    /**
     * ���xml�ڵ��ȡ����ڵ�
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            String ���ڵ�����
     * @return ����ֵ��List<String>���ӽڵ��list
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static List<String> getChildListValuesByTagName(Element ele, String childEleName)
    {

        NodeList nl = ele.getChildNodes();
        List<String> childEles = new ArrayList<String>();
        for (int i = 0; i < nl.getLength(); i++)
        {
            Node node = nl.item(i);
            if (node instanceof Element && childEleName.equals(node.getNodeName())
                    || childEleName.equals(node.getLocalName()))
            {
                childEles.add(getTextValue((Element) node));
            }
        }
        return childEles;
    }

    /**
     * ���xml�ڵ��ȡ��Ӧ��������
     * 
     * @param ����˵��
     *            Element ��xml��һ���ڵ�
     * @param ����˵��
     *            String ����������
     * @return ����ֵ��String ������ֵ
     * @exception �쳣��ע�ͳ�ʲô�����»���ʲô����쳣
     * @see �ο���JavaDoc
     */
    public static String getAttrValuesByName(Element ele, String attributeName)
    {

        return ele.getAttribute(attributeName);
    }
}
