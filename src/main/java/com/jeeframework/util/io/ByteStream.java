package com.jeeframework.util.io;


import com.jeeframework.util.io.bs.*;

import java.nio.ByteBuffer;
import java.util.*;



/**
 * ���л���
 * 
 * 
 */

public final class ByteStream
{
    
     // ���л�ʱ�����buf
	private byte[]						m_byPackage		= null;
	
	// ���л�ʱbuf�ĵĳ���
	private int								m_iBufLen			= 0;
	// buf��ƫ��ֵ
	private int								m_iOffset			= 0;
	private boolean						m_bGood				= true;
	private boolean						m_bRealWrite	= true;

	/**
	 * �����buffer����󳤶�Ϊ1M.
	 * 
	 */
	private final static int MAX_LENGTH= 1024*1024;
	
	/**
	 * 
	 *����Ƚ�����
	 *��Ϊjava��long�����з�ŵģ���c++��uint64_t���޷�ŵģ�����java��longװ����uint64_t��
	 *���������л�������
	 *����ʽ���ġ�����ѹ��һ�����ȣ���ѹһ��long
	 *
	 *Ҳ������ĳ��Ⱦ�Ϊ17��
	 *
	 */
	private final static int	LONGLENGTH		= 17;
    
	/**
	 * Ĭ�Ϲ���
	 */
	public ByteStream()
	{
	}
    /**
     * ��ʹ��buf
     * 
     * ����һ���ⲿbuf�������л���buf
     * 
     * @param byBuffer �ⲿ����õ�buf  
     * @param iBufLen  buf�ĳ���
     * 
     */
	public ByteStream(byte[] byBuffer, int iBufLen)
	{
		resetBuffer(byBuffer, iBufLen);
	}

	/**
	 * ��bytestream�󶨵�ָ����byte buf����
	 * 
	 * @param byBuffer ָ����buff
	 *            
	 * @param iBufLen ָ����buff�ĳ���
	 */
	public void resetBuffer(byte[] byBuffer, int iBufLen)
	{
		
		m_byPackage = byBuffer;
		
		
		m_iBufLen = iBufLen;

	
		m_iOffset = 0;
		m_bGood = true;
		m_bRealWrite = true;
	}
    /**
     * ����buf�Ƿ��д
     * һ�����ڼ���buf�ĳ���ʱ��
     * 
     * @param bRealWrite �Ƿ��ǿ�дtrue or false
     * 
     */
	public void setRealWrite(boolean bRealWrite)
	{
		m_bRealWrite = bRealWrite;
	}
    /**
     * 
     * ���� ��д��ƫ��ֵ
     * 
     * @return m_iOffset ƫ��ֵ
     */
	public int getWrittenLength()
	{
		return m_iOffset;
	}
    
	/**
     * 
     * ���ؿɶ���ƫ��ֵ
     * 
     * @return m_iOffset ƫ��ֵ
     */
	public int getReadLength()
	{
		return m_iOffset;
	}
    
	/**
	 * buf�Ƿ����
	 * 
	 * @return buf�Ƿ����
	 */
	public boolean isGood()
	{
		return m_bGood;
	}

	/**
	 * ���л�int8
	 * 
	 * @param vҪ���л���byte
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushByte(byte v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 1) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(byte v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 1) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset++;
			return;
		}

		m_byPackage[m_iOffset++] = v;
	}
    
	/**
     * �����л�int8
     * 
     * @return �����к��byte
     * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
     */
	public byte popByte() throws Exception
	{
		if (!m_bGood || (m_iOffset + 1) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popByte ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 1) > m_iBufLen["+m_iBufLen+"]");
		}
		return m_byPackage[m_iOffset++];
	}
    
	/**
	 * ���л�һ��booleanֵ
	 * 
	 * @param v Ҫ���л���booleanֵ
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushBoolean(boolean v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 1) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(byte v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 1) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset++;
			return;
		}
		pushByte((byte) (v ? 0x1 : 0x0));
	}

	/**
	 * �����л�һ��booleanֵ
	 * 
	 * @return �����л���booleanֵ
	 */
	public boolean popBoolean() throws Exception
	{
		return popByte() == (byte) 0x01;
	}

	/**
	 * ���л�һ��booleanֵ
	 * 
	 * @param v Ҫ���л���booleanֵ
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushBool(boolean v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 1) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(byte v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 1) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset++;
			return;
		}
		pushByte((byte) (v ? 0x1 : 0x0));
	}

	/**
	 * �����л�һ��booleanֵ
	 * 
	 * @return �����л���booleanֵ
	 */
	public boolean popBool() throws Exception
	{
		return popByte() == (byte) 0x01;
	}

	/**
	 * ���л�uint8_t
	 * @param v Ҫ���л���uint8_tֵ����������һ��short��
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUByte(short v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 1) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(byte v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 1) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset++;
			return;
		}

		m_byPackage[m_iOffset++] = (byte) (v);
	}

	/**
	 * �����л�uint8_t
	 * 
	 * @return v �����л���uint8_tֵ����������һ��short��
	 * 
	 */
	public short popUByte() throws Exception
	{
		return (short) (0x00FF & (popByte()));
	}

	/**
	 * ���л�int16
	 * 
	 * @param v Ҫ���л���int16ֵ����������һ��short��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushShort(short v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 2) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(short v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 2) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += 2;
			return;
		}

		m_byPackage[m_iOffset++] = (byte) (v >> 8);
		m_byPackage[m_iOffset++] = (byte) (v);
	}

	/**
	 * �����л�int16
	 * 
	 * @return v �����л���int16ֵ����������һ��short��
	 *  @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public short popShort() throws Exception
	{
		if (!m_bGood || (m_iOffset + 2) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popShort ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 2) > m_iBufLen["+m_iBufLen+"]");
		}
		short v = (short)(m_byPackage[m_iOffset++] & 0xff);
		v = (short) (v << 8);
		v |= (m_byPackage[m_iOffset++] & 0xff);
		return v;
	}

	/**
	 * ���л�uint16_t
	 * 
	 * @param v Ҫ���л���uint16_tֵ����������һ��int��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUShort(int v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 2) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(short v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 2) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += 2;
			return;
		}

		m_byPackage[m_iOffset++] = (byte) (v >> 8);
		m_byPackage[m_iOffset++] = (byte) (v);
	}

	/**
	 * �����л�uint16_t
	 * 
	 * @return v �����л���uint16_tֵ����������һ��int��
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public int popUShort() throws Exception
	{
		if (!m_bGood || (m_iOffset + 2) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popUShort ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 2) > m_iBufLen["+m_iBufLen+"]");
		}
		int firstByte = (0x000000FF & (m_byPackage[m_iOffset++]));
		int secondByte = (0x000000FF & (m_byPackage[m_iOffset++]));
		return (firstByte << 8 | secondByte);
	}

	/**
	 * ���л�int32
	 * 
	 * @param v Ҫ���л���int32ֵ����������һ��int��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushInt(int v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 4) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(int v) ERROR: !m_bGood || (m_iOffset["+m_iOffset+"] + 4) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += 4;
			return;
		}

		m_byPackage[m_iOffset++] = (byte) (v >> 24);
		m_byPackage[m_iOffset++] = (byte) (v >> 16);
		m_byPackage[m_iOffset++] = (byte) (v >> 8);
		m_byPackage[m_iOffset++] = (byte) (v);
	}

	/**
	 * �����л�int32
	 * 
	 * @return v �����л���int32ֵ����������һ��int��
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public int popInt() throws Exception
	{
		if (!m_bGood || (m_iOffset + 4) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popInt ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 4) > m_iBufLen["+m_iBufLen+"]");
		}
		int v = m_byPackage[m_iOffset++] & 0xff;
		for (int i = 0; i < 3; ++i)
		{
			v = v << 8;
			v |= (m_byPackage[m_iOffset++] & 0xff);
		}
		
		return v;
	}

	/**
	 * ���л�uint32_t
	 * 
	 * @param v Ҫ���л���uint32_tֵ����������һ��long��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUInt(long v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 4) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(int v) ERROR: !m_bGood || (m_iOffset["+m_iOffset+"] + 4) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += 4;
			return;
		}

		m_byPackage[m_iOffset++] = (byte) (v >> 24);
		m_byPackage[m_iOffset++] = (byte) (v >> 16);
		m_byPackage[m_iOffset++] = (byte) (v >> 8);
		m_byPackage[m_iOffset++] = (byte) (v);
	}
    
	/**
	 * 
	 * @return  Ҫ���л���uint32_tֵ����������һ��long��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public long popUInt() throws Exception
	{
		if (!m_bGood || (m_iOffset + 4) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popUInt ERROR:  !m_bGood " + m_bGood+ " || (m_iOffset["+m_iOffset+"] + 4) > m_iBufLen["+m_iBufLen+"]");
		}

		long firstByte = (0x000000FF & ((long) m_byPackage[m_iOffset++]));
		long secondByte = (0x000000FF & ((long) m_byPackage[m_iOffset++]));
		long thirdByte = (0x000000FF & ((long) m_byPackage[m_iOffset++]));
		long fourthByte = (0x000000FF & ((long) m_byPackage[m_iOffset++]));

		return ((firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	/**
	 * ���л�int64
	 * 
	 * @param v Ҫ���л���int64ֵ����������һ��long��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushLong(long v) throws Exception
	{
		if (!m_bGood || (m_iOffset + 17) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("push(long v) ERROR:  !m_bGood || (m_iOffset["+m_iOffset+"] + 8) > m_iBufLen["+m_iBufLen+"]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += 17;
			return;
		}
		if(v < 0)
			throw new Exception("push(long v) ERROR:  v["+v+"] must > 0");

		String s = String.format("%16s", Long.toHexString(v)).replace(' ', '0');
		pushBytes(s.getBytes(), 16);
		pushByte((byte) 0x0);
	}

	/**
	 * ������pop��һ��long �����Ӧint64
	 *  
	 * @return ��һ��long ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public long popLong() throws Exception
	{


		byte[] bs = popBytes(16);
		popByte();
		if (bs == null)
			return 0;

		return Long.parseLong(new String(bs), 16);
	}

	/**
	 * ���л�String
	 * 
	 * @param  s �����л����ַ�
	 * 
	 * @throws Exception  ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushString(String s) throws Exception
	{
		if (validateInput(s))
			pushBytes(s.getBytes());
		else
			pushBytes(null);
	}
    
	/**
	 * �����л��ַ�
	 * 
	 * @return �����л����ַ�
	 * 
	 * @throws Exception  ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public String popString() throws Exception
	{
		if (!m_bGood || (m_iOffset + 4) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popBytes ERROR: !m_bGood || (m_iOffset["+m_iOffset+"] + 4) > m_iBufLen["+m_iBufLen+"]");
		}
		//
		int len = (int) popUInt();
		if (!m_bGood || (m_iOffset + len) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popBytes ERROR: !m_bGood || (m_iOffset[" + m_iOffset + "] + len[" + len + "]) > m_iBufLen[" + m_iBufLen + "]");
		}
        if(len > MAX_LENGTH){
        	m_bGood = false;
        	throw new Exception("len > MAX_LENGTH");
        }
		if (len > 0)
		{
			byte[] buf = new byte[len];
			System.arraycopy(m_byPackage, m_iOffset, buf, 0, len);
			m_iOffset += len;
			return new String(buf);
		}
		else
			return "";
	}

	/**
	 * ���л�һ��bytes����
	 * 
	 * @param buf �����л������
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushBytes(byte[] buf, int iLen) throws Exception
	{
		if (!m_bGood || iLen < 0 || (m_iOffset + iLen) > m_iBufLen || iLen > MAX_LENGTH)
		{
			m_bGood = false;
			throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood || iLen <=0 || (m_iOffset[" + m_iOffset + "] + ilen[" + iLen + "]) > m_iBufLen[" + m_iBufLen + "]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += iLen;
			return;
		}

		System.arraycopy(buf, 0, m_byPackage, m_iOffset, iLen);
		m_iOffset += iLen;
	}

	/**
	 * ���л�һ��bytes���顣�����л���ĳ��ȣ������л�����
	 * 
	 * @param buf �����л������
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushBytes(byte[] buf) throws Exception
	{
	

		if (!m_bRealWrite)
		{
			m_iOffset += ByteStream.getObjectSize(buf);
			return;
		}

        if (!m_bGood || (m_iOffset + ByteStream.getObjectSize(buf)) > m_iBufLen)
        {
            m_bGood = false;
            throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood ||  (m_iOffset[" + m_iOffset + "] +  iLen[" + buf.length + "]) > m_iBufLen:" + "[" + m_iBufLen + "]");
        }
        
		if ((buf == null) || (buf.length == 0))
		{

			pushInt(0);
		}else if(buf.length > MAX_LENGTH)
		  throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  buf.length > MAX_LENGTH");
		else{

			pushInt(buf.length);
			pushBytes(buf, buf.length);
		}
		
	}

	/**
	 * ���л�һ�������������л���ĳ��ȣ������л������е�ÿһ��Ԫ������
	 * 
	 * @param values �����л������
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushVector(Vector<?> values) throws Exception
	{
		if (!m_bGood || (m_iOffset + ByteStream.getVectorSize(values)) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood || iLen <=0 || (m_iOffset[" + m_iOffset + "] +  iLen[" +ByteStream.getVectorSize(values) + "]) > m_iBufLen:" + "[" + m_iBufLen + "]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += ByteStream.getVectorSize(values);
			return;
		}
		if (values == null||ByteStream.getVectorSize(values)<=4)
		{
			pushInt(0);
		}
		else
		{
			pushInt(values.size());
			for (int i = 0; i < values.size(); i++)
				pushObject(values.elementAt(i));
		}
	}

	/**
	 * ���л�һ�����?�����л���ĳ��ȣ������л������е�ÿһ��Ԫ������
	 * 
	 * @param values �����л������
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushList(List<?> values) throws Exception
	{

		if (!m_bGood || (m_iOffset + ByteStream.getListSize(values)) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood || iLen <=0 || (m_iOffset[" + m_iOffset + "] +  iLen[" +ByteStream.getListSize(values) + "]) > m_iBufLen:" + "[" + m_iBufLen + "]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += ByteStream.getListSize(values);
			return;
		}
		if (!validateInput(values) || ByteStream.getListSize(values)<=4)
		{
			pushInt(0);
		}
		else
		{
			pushInt(values.size());
			for (int i = 0; i < values.size(); i++)
				pushObject(values.get(i));
		}
	}

	/**
	 * �����л� byte[]
	 * 
	 * @param iLen Ҫ�����л������ĳ���
	 * @return byte[]
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public byte[] popBytes(int iLen) throws Exception
	{
		if (iLen < 0 || iLen > MAX_LENGTH)
		{
			throw new Exception("popBytes ERROR: len must >= 0 now value = " + iLen);
		}

		if (!m_bGood || (m_iOffset + iLen) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("popBytes ERROR: !m_bGood || (m_iOffset[" + m_iOffset + "] + ilen[" + iLen + "]) > m_iBufLen[" + m_iBufLen + "]");
		}

		if (iLen > 0)
		{
			byte[] buf = new byte[iLen];
			System.arraycopy(m_byPackage, m_iOffset, buf, 0, iLen);
			m_iOffset += iLen;
			return buf;
		}
		else
			return "".getBytes();
	}

	/**
	 * �����л�һ��byte���飬�ȷ��򻯳��ȣ�Ȼ���ٷ��򻯳��ȴ��ڵ�bytes
	 * 
	 * @return byte[]
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public byte[] popBytes() throws Exception
	{
		int len = popInt();
		if(len > MAX_LENGTH)
			throw new Exception("popBytes ERROR: len > MAX_LENGTH");
		if (len > 0)
			return popBytes(len);
		else
			return "".getBytes();
	}

	/**
	 * �����л�һ���������ȷ��򻯳��ȣ�Ȼ��bytes�е�����
	 * 
	 * @return ret Vector<?>
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public Vector<?> popVector(Class<?> clazz) throws Exception
	{

		Vector<Object> ret = new Vector<Object>();
		int len = popInt();
		for (int i = 0; i < len; i++)
		{
			Object o = popObject(clazz);
			ret.add(o);
		}
		return ret;
	}

	/**
	 * 
	 * @param collectionClazz:List������
	 * @param elementClazz��List�����е�Ԫ�ص�����
	 * @return ret List<?>
	 * @throws Exception������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	@SuppressWarnings("unchecked")
	public List<?> popList(Class<?> collectionClazz, Class<?> elementClazz) throws Exception
	{
		List<Object> ret = (List<Object>) collectionClazz.newInstance();
		int len = popInt();
		for (int i = 0; i < len; i++)
		{
			Object o = popObject(elementClazz);
			ret.add(o);
		}
		return ret;
	}

	/**
	 * ��16���Ƶķ�ʽ��ӡbuf���ֵ
	 */
	public void dump()
	{
		for (int i = 0; i < m_iBufLen; ++i)
		{
			System.out.print(String.format("%02x", m_byPackage[i]) + " ");
			if ((i + 1) % 16 == 0)
				System.out.println("");
		}

		System.out.println();
	}
    
	/**
	 * ����һ��ByteBuffer
	 * 
	 * @return byteBuffer
	 */
	public ByteBuffer asByteBuffer()
	{
		return ByteBuffer.wrap(m_byPackage);
	}

	/**
	 * �����ĳ���
	 * 
	 * @param value byte[]
	 * @return size ����
	 */
	public static int bytesLength(byte[] value)
	{
		return 4 + (value != null ? value.length : 0);
	}

	/**
	 * ��ȡĳ����������л�����
	 * 
	 * @param object:��null
	 * @return ����ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	public static int getObjectSize(Object object) throws Exception
	{
		if (!validateInput(object))
		{
			return 4;
		}
		else
		{
			if (object instanceof JeeSerializable)
				return ((JeeSerializable) object).getSize();
			else if (object instanceof String)
				return 4 + ((String) object).getBytes().length;
			else if (object instanceof byte[])
				return 4 + ((byte[]) object).length;
			else if (object instanceof Long)
			{
				return getLongLongSize();
			}
			else if (object instanceof uint16_t)
			{
				return 2;
			}
			else if (object instanceof uint8_t)
			{
				return 1;
			}
			else if (object instanceof uint32_t)
			{
				return 4;
			}
			
			else if (object instanceof Integer)
				return 4; 
			else if (object instanceof Short)
				return 1;
			else if (object instanceof Byte)
				return 1;
			else if (object instanceof Vector<?>)
				return getVectorSize(object);
			else if (object instanceof List<?>)
				return getListSize(object);
			else if (object instanceof MultiMap)
				return getMultiMapSize(object);
			else if (object instanceof Map<?, ?>)
				return getMapSize(object);

			else
				throw new Exception("��֧�ֵ�������ͣ�" + object.getClass().getName());
		}
	}
    
	/**
	 * ��ȡMap��������л�����
	 * 
	 * @param object:��null
	 * @return Map�ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	private static int getMapSize(Object object) throws Exception
	{
		if (!validateInput(object))
		{
			return 4;
		}
		else
		{
			int l = 4;
			Map<?, ?> map = (Map<?, ?>) object;
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
				l += getObjectSize(entry.getKey());
				l += getObjectSize(entry.getValue());
			}

			return l;
		}
	}
    
	/**
	 * ��ȡMultiMap��������л�����
	 * 
	 * @param object:��null
	 * @return MultiMap�ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	private static int getMultiMapSize(Object object) throws Exception
	{
		if (!validateInput(object))
		{
			return 4;
		}
		else
		{
			int l = 4;
			MultiMap<?, ?> map = (MultiMap<?, ?>) object;
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();

				Collection<?> v = (Collection<?>) entry.getValue();
				Iterator<?> xx = v.iterator();
				while (xx.hasNext())
				{
					l += getObjectSize(entry.getKey());
					l += getObjectSize(xx.next());
				}
			}
			return l;
		}
	}
 
	/**
	 * ��ȡVector��������л�����
	 * 
	 * @param object:��null
	 * @return Vector�ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	private static int getVectorSize(Object object) throws Exception
	{
		if (!validateInput(object))
		{
			return 4;
		}
		else
		{
			int l = 4;
			Vector<?> vector = (Vector<?>) object;
			for (int i = 0; i < vector.size(); i++)
			{
				l += getObjectSize(vector.elementAt(i));
			}
			return l;
		}
	}


	/**
	 * ��ȡuint64_t��������л�����
	 * 
	 * @return uint64_t�ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	private static int getLongLongSize() throws Exception
	{
		return LONGLENGTH;
	}
    
	 
	/**
	 * ��ȡList��������л�����
	 * 
	 * @param object:��null
	 * @return List�ĳ��� ����
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	private static int getListSize(Object object) throws Exception
	{
		if (!validateInput(object))
		{
			return 4;
		}
		else
		{
			int l = 4;
			List<?> list = (List<?>) object;
			for (int i = 0; i < list.size(); i++)
			{
				l += getObjectSize(list.get(i));
			}
			return l;
		}
	}

	/**
	 * ���л�һ������
	 * ��֧�ֶ��ط���
	 * 
	 * @param object �����л��Ķ���
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	@SuppressWarnings("unchecked")
	public void pushObject(Object object) throws Exception
	{

		if (!validateInput(object)){
			pushInt(0);
			return ;
		}

		if (object instanceof JeeSerializable)

			((JeeSerializable) object).serialize(this);

		else if (object instanceof Vector<?>)
			pushVector((Vector<Object>) object);
		else if (object instanceof List<?>)
			pushList((List<Object>) object);
		else if (object instanceof MultiMap)
			pushMultiMap((MultiMap) object);
		else if (object instanceof Map<?, ?>)
			pushMap((Map<Object, Object>) object);
		else if (object instanceof byte[])
			pushBytes((byte[]) object);
		else if (object instanceof String)
			pushString((String) object);
		else if (object instanceof Integer)
			pushInt((Integer) object);
		else if (object instanceof uint16_t)
			pushUShort((Integer) ((uint16_t)object).getValue());
		else if (object instanceof uint8_t){
			pushUByte((short)((uint8_t)object).getValue());
		}
		else if (object instanceof uint32_t)
			pushUInt((Long) ((uint32_t)object).getValue());
		else if (object instanceof Short)
			pushUByte((Short) object);
		else if (object instanceof Long)
			pushUInt((Long) object);
		else
			throw new Exception("��֧�ֵ����л�����" + object.getClass().getName());
	}

	/**
	 * ���л�һ�� Object
	 * 
	 * ��֧�ֶ��ط���
	 * 
	 * @param clazz ������Ĳ����� String.class
	 * @return Object
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	@SuppressWarnings("cast")
	public Object popObject(Class<?> clazz) throws Exception
	{
		Object ret = null;
		if (!(clazz == byte[].class || clazz == Integer.class || clazz == Long.class || clazz == Short.class || clazz == uint8_t.class || clazz == uint16_t.class || clazz == uint32_t.class ))
			 ret = clazz.newInstance();
			
		if (ret instanceof JeeSerializable)
			((JeeSerializable) ret).unSerialize(this);
		else if (ret instanceof Vector<?>)
			ret = popVector(clazz);
		else if (clazz == byte[].class)
			ret = popBytes();
		else if (clazz == String.class)
			ret = popString();
		else if (clazz == Integer.class)
			ret = popInt();
		else if (clazz == uint8_t.class)
			ret = popUByte();
		else if (clazz == uint16_t.class)
			ret = popUShort();
		else if (clazz == uint32_t.class)
			ret = popUInt();
		else if (clazz == Short.class)
			ret = popUByte();
		else if (clazz == Long.class)
			ret = popUInt();
		else
			throw new Exception("��֧�ֵķ����л�����" + clazz.getName());

		return ret;
	}
	
	/**
	 * ֧�ֶ��ط�Χ��map�����л�
	 * 
	 * @param bs
	 * @param keyGeneric �������� @see com.jeeframework.lang.GenericWrapper;
	 * @param valueGeneric �������� @see com.jeeframework.lang.GenericWrapper;
	 * @return
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	// new Class[]{String.class, {HashMap.class, {String.class, String.class}}}
	public  Map<?, ?> popMap(GenericWrapper keyGeneric, GenericWrapper valueGeneric) throws Exception {
		Map<Object, Object> ret = new HashMap<Object, Object>();

		int l = popInt();
		for (int i = 0; i < l; i++) {
			Object k = null;
			Object v = null;

			// �õ�key
			k = popObject(keyGeneric);

			v = popObject(valueGeneric);

			ret.put(k, v);
		}

		return ret;
	}

	/**
	 *  ֧�ֶ��ط�Χ��object�����л�
	 * 
	 * @param bs
	 * @param keyGeneric �������� @see com.jeeframework.lang.GenericWrapper;
	 * @return
	 * @throws Exception  ������д���߳��ȳ�����д����ʱ�׳��쳣 ��
	 */
	public  Object popObject(GenericWrapper keyGeneric) throws Exception {
		Class<?> type = keyGeneric.getType();
		Object value = new Object();
		if (type == String.class) {
			value = popString();
		} else if (type == Long.class) {
			value = popUInt();
		} else if (type == uint8_t.class) {
			value = popUByte();
		}  else if (type == uint16_t.class) {
			value = popUShort();
		}  else if (type == uint32_t.class) {
			value = popUInt();
		}  else if (type == Integer.class) {
			value = popInt();
		} else if (type == byte[].class) {
			value = popBytes();
		} else if (type == Vector.class) {
			value = popVector( keyGeneric.getGenericParameters()[0]);
		} else if (type == List.class || type == ArrayList.class) {
			value = popList(ArrayList.class, keyGeneric.getGenericParameters()[0]);
		}

		else if (type == HashMap.class) {
			value = popMap( keyGeneric.getGenericParameters()[0], keyGeneric.getGenericParameters()[1]);
		} else if (type == MultiMap.class) {
			value = popMultiMap( keyGeneric.getGenericParameters()[0], keyGeneric.getGenericParameters()[1]);
		} else {
			try {
				value = type.newInstance();
				if (value instanceof JeeSerializable) {
					((JeeSerializable) value).unSerialize(this);

				} else
					value = null;
			} catch (Exception e) {
				e.printStackTrace();
				value = null;
			}
		}
		return value;
	}
    
	/**
	 *  ֧�ֶ��ط�Χ��List�����л�
	 * 
	 * 
	 * @param collectionClazz ArrayList.class
	 * @param keyGeneric �������� @see com.jeeframework.lang.GenericWrapper;
	 * @return
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 ��
	 */
	// new Class[]{String.class, {HashMap.class, {String.class, String.class}}}
	@SuppressWarnings("unchecked")
	public  List<?> popList( Class<?> collectionClazz, GenericWrapper keyGeneric) throws Exception {
		List<Object> ret = (List<Object>) collectionClazz.newInstance();

		int l = popInt();
		for (int i = 0; i < l; i++) {
			Object value = null;
			value = popObject(keyGeneric);
			ret.add(value);
		}

		return ret;
	}
    /**
     *  ֧�ֶ��ط�Χ��Vector�����л�
     * 
     * 
     * @param keyGeneric ���Ͱ�� @see com.jeeframework.lang.GenericWrapper;
     * @return
     * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 ��
     */
	public  Vector<?> popVector(GenericWrapper keyGeneric) throws Exception {
		Vector<Object> ret = new Vector<Object>();

		int l = popInt();
		for (int i = 0; i < l; i++) {
			Object value = null;
			value = popObject(keyGeneric);
			ret.add(value);
		}

		return ret;
	}
    
	/**
	 *  ֧�ֶ��ط�Χ��MultiMap�����л�
	 * 
	 * @param keyGeneric ���Ͱ�� @see com.jeeframework.lang.GenericWrapper;
	 * @param valueGeneric ���Ͱ�� @see com.jeeframework.lang.GenericWrapper;
	 * @return
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 ��
	 */
	// new Class[]{String.class, {HashMap.class, {String.class, String.class}}}
	public  MultiMap<?, ?> popMultiMap(GenericWrapper keyGeneric, GenericWrapper valueGeneric) throws Exception {
		MultiMap<Object, Object> ret = new MultiMap<Object, Object>();

		int l = popInt();
		for (int i = 0; i < l; i++) {
			Object k = null;
			Object v = null;

			k = popObject(keyGeneric);

			v = popObject(valueGeneric);

			ret.put(k, v);
		}

		return ret;
	}
	
	
	/**
	 * ���л�һ�� map
	 * 
	 * @param map �����л������
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	public void pushMap(Map<?, ?> map) throws Exception
	{
		if (!m_bGood || (m_iOffset + ByteStream.getMapSize(map)) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood || iLen <=0 || (m_iOffset[" + m_iOffset + "] + ilen[" + ByteStream.getMapSize(map) + "]) > m_iBufLen[" + m_iBufLen + "]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += ByteStream.getMapSize(map);
			return;
		}
		if (!validateInput(map)||ByteStream.getMapSize(map) <= 4)
		{
			pushInt(0);
		}
		else
		{
			// ����
			pushInt(map.size());
			// ÿһ��Ԫ�ؽ���push
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
				pushObject(entry.getKey());
				pushObject(entry.getValue());
			}
		}
	}

	/**
	 * ���л���һ�� map
	 * 
	 * @param keyclass  ������Ĳ����� String.class
	 * @param dataClass   ������Ĳ����� String.class
	 * @return map �����л��õ���map
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	public Map<?, ?> popMap(Class<?> keyclass, Class<?> dataClass) throws Exception
	{
		HashMap<Object, Object> ret = new HashMap<Object, Object>();

		// �ȵõ�����
		int len = popInt();
		for (int i = 0; i < len; i++)
		{
			Object key = popObject(keyclass);
			Object value = popObject(dataClass);

			ret.put(key, value);
		}

		return ret;
	}
    
	/**
	 * ���л�һ�� multimap
	 * 
	 * @param map �����л������
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	public void pushMultiMap(MultiMap<?, ?> map) throws Exception
	{
		if (!m_bGood || (m_iOffset + ByteStream.getMultiMapSize(map)) > m_iBufLen)
		{
			m_bGood = false;
			throw new Exception("pushBytes(byte[] buf, int iLen) ERROR:  !m_bGood || iLen <=0 || (m_iOffset[" + m_iOffset + "] + ilen[" + ByteStream.getMultiMapSize(map) + "]) > m_iBufLen[" + m_iBufLen + "]");
		}
		if (!m_bRealWrite)
		{
			m_iOffset += ByteStream.getMultiMapSize(map);
			return;
		}
		if (!validateInput(map)|| ByteStream.getMultiMapSize(map) <= 4)
		{
			pushInt(0);
		}
		else
		{
			// ����
			pushInt(map.totalSize());
			// ÿһ��Ԫ�ؽ���push
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
				// push key �� value
				Collection<?> v = (Collection<?>) entry.getValue();
				Iterator<?> xx = v.iterator();
				while (xx.hasNext())
				{
					pushObject(entry.getKey());
					pushObject(xx.next());
				}
			}
		}
	}

	/**
	 * popһ�� multimap
	 * 
	 * @param keyclass ������Ĳ����� String.class
	 * @param dataClass ������Ĳ����� String.class
	 * @return ret  MultiMap
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣 
	 */
	@SuppressWarnings("unchecked")
	public MultiMap<?, ?> popMultiMap(Class keyclass, Class dataClass) throws Exception
	{
		MultiMap<Object, Object> ret = new MultiMap<Object, Object>();

		// �ȵõ�����
		int len = popInt();
		for (int i = 0; i < len; i++)
		{
			Object key = popObject(keyclass);
			Object value = popObject(dataClass);
			//
			ret.put(key, value);
		}

		return ret;
	}

	/**
	 * �Դ������ĵĲ������У��
	 * 
	 * @param o ����
	 * @return boolean �Ƿ���nullֵ
	 */
	private static boolean validateInput(Object object)
	{
		return null != object;
	}
	
	/**
	 * ���л�uint8_t
	 * 
	 * @param v Ҫ���л���uint8_tֵ����������һ��uint8_t��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUInt8_t(uint8_t v) throws Exception
	{
		pushUByte((short)v.getValue());
	}
	
	/**
	 * �����л�uint8_t
	 * 
	 * @return v �����л���uint8_tֵ����������һ��short��
	 * 
	 */
	public uint8_t popUint8_t() throws Exception
	{
		uint8_t val = new uint8_t();
		val.setValue(popUByte());
		
		return val;
	}
	
	
	/**
	 * ���л�uint16_t
	 * 
	 * @param v Ҫ���л���uint16_tֵ����������һ��uint16_t��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUInt16_t(uint16_t v) throws Exception
	{
		pushUShort((int)v.getValue());
	}
	
	/**
	 * �����л�uint16_t
	 * 
	 * @return v �����л���uint16_tֵ����������һ��short��
	 * 
	 */
	public uint16_t popUint16_t() throws Exception
	{
		uint16_t val = new uint16_t();
		val.setValue(popUShort());
		
		return val;
	}
	
	/**
	 * ���л�uint32_t
	 * 
	 * @param v Ҫ���л���uint32_tֵ����������һ��uint32_t��
	 * 
	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
	 */
	public void pushUInt32_t(uint32_t v) throws Exception
	{
		pushUInt((int)v.getValue());
	}
	
	/**
	 * �����л�uint32_t
	 * 
	 * @return v �����л���uint32_tֵ����������һ��short��
	 * 
	 */
	public uint32_t popUint32_t() throws Exception
	{
		uint32_t val = new uint32_t();
		val.setValue(popUInt());
		
		return val;
	}
	
//	/**
//	 * ���л�uint64_t
//	 * 
//	 * @param v Ҫ���л���uint64_tֵ����������һ��uint64_t��
//	 * 
//	 * @throws Exception ������д���߳��ȳ�����д����ʱ�׳��쳣
//	 */
//	public void pushUInt64_t(uint64_t v) throws Exception
//	{
//		pushUInt((int)v.getValue());
//	}
//	
//	/**
//	 * �����л�uint64_t
//	 * 
//	 * @return v �����л���uint64_tֵ����������һ��short��
//	 * 
//	 */
//	public uint64_t popUint64_t() throws Exception
//	{
//		uint64_t val = new uint64_t();
//		val.setValue(popUInt());
//		
//		return val;
//	}
	
}
