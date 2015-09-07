package com.jeeframework.util.mime;

import java.io.IOException;
import java.util.Properties;



/**
 * Mimetype���� ���ڹ����ļ���׺���mimetype ����ͨ�����ļ����ö�Ӧ��mimetype
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 */
public class MimetypesMap
{
    /**
     * mimetype������
     */
    private Properties _map = null;

    private MimetypesMap()
    {
        _map = loadMimetypesMap("/com/linktong/util/mime/mimetypes.map");
    }

    private static MimetypesMap _oMimetypesMap = new MimetypesMap();

    /**
     * ����singleton����
     * 
     * @return MimetypesMap
     */
    public static MimetypesMap instance()
    {
        return _oMimetypesMap;
    }

    /**
     * �Ӹ��·���л�ȡ������
     * 
     * @param inMimetypesMap
     *            String
     * @return Properties
     */
    private static Properties loadMimetypesMap(String inMimetypesMap)
    {
        Properties map = null;

        Class mm;
        try
        {
            mm = Class.forName("com.jeeframework.sdk.util.mime.MimetypesMap");

            map = new Properties();
            java.io.InputStream is = mm.getResourceAsStream(inMimetypesMap);
            if (is != null)
            {
                map.load(is);
                is.close();
            }
        } catch (ClassNotFoundException e)
        {

            throw new RuntimeException(e);
        } catch (IOException e)
        {

            throw new RuntimeException(e);
        }

        return map;
    }

    /**
     * �����ļ������Ӧ��mimetype
     * 
     * @param inSuffix
     *            String
     * @return String
     */
    public String getMimeType(String inSuffix)
    {

        String sMimeType = null;
        if (inSuffix != null
                && (sMimeType = _map.getProperty(inSuffix.toLowerCase())) != null)
        {
            return sMimeType;
        }
        return "application/octet-stream";

    }
}
