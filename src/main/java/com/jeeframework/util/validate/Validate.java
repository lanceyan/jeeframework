package com.jeeframework.util.validate;


import com.jeeframework.util.string.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * У���࣬��ݸ�������ж��Ƿ�ɹ�
 * 
 * @author lanceyan�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * @see �ο���JavaDoc
 */

public class Validate
{
    public static final String emailAddressPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    public static boolean isNull(Object object)
    {
        if (object == null)
        {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str)
    {
        if (null == str || str.trim().length() == 0)
        {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object[] array)
    {
        if (array == null || array.length == 0)
        {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection collection)
    {
        if (collection == null || collection.size() == 0)
        {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Map map)
    {
        if (map == null || map.size() == 0)
        {
            return true;
        }
        return false;
    }
    public static boolean isUnicodeLetter(String str)
    {
        return StringUtils.isAlpha( str);
    }
    
    public static boolean isLong(String str)
    {
        if (isEmpty(str))
        {
            return false;
        }
        try
        {
            Long.valueOf(str);
        } catch (NumberFormatException ne)
        {
            return false;
        }
        return true;
    }
    
    
    public static boolean isAlpha(String str)
    {
        return StringUtils.isAlphaNotCHN( str);
    }

    public static boolean isAlphaNumeric(String str)
    {
        return StringUtils.isAlphaNumericNotCHN(str);
    }
    
    public static boolean validateByteLength(String is, int islength)
    {
        if (null == is)
        {
            return false;
        }

        if (is.getBytes().length <= islength)
        {
            return true;
        }
        return false;
    }

    public static boolean validateLength(String is, int length)
    {
        if (null == is)
        {
            return false;
        }

        if (is.length() <= length)
        {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Checks if the String contains only unicode digits. A decimal point is not a unicode digit and returns false.
     * </p>
     * 
     * <p>
     * <code>null</code> will return <code>false</code>. An empty String ("") will return <code>true</code>.
     * </p>
     * 
     * <pre>
     * StringUtilss.isNumeric(null)   = false
     * StringUtilss.isNumeric(&quot;&quot;)     = true
     * StringUtilss.isNumeric(&quot;  &quot;)   = false
     * StringUtilss.isNumeric(&quot;123&quot;)  = true
     * StringUtilss.isNumeric(&quot;12 3&quot;) = false
     * StringUtilss.isNumeric(&quot;ab2c&quot;) = false
     * StringUtilss.isNumeric(&quot;12-3&quot;) = false
     * StringUtilss.isNumeric(&quot;12.3&quot;) = false
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str)
    {
        return StringUtils.isNumeric(str);
    }

    /**
     * <p>
     * Checks if the value can safely be converted to a byte primitive.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return truGenericTypeValidator.javae if the value can be converted to a Byte.
     */
    public static boolean isByte(String value)
    {
        return (GenericTypeValidator.formatByte(value) != null);
    }

    /**
     * <p>
     * Checks if the value can safely be converted to a short primitive.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return true if the value can be converted to a Short.
     */
    public static boolean isShort(String value)
    {
        return (GenericTypeValidator.formatShort(value) != null);
    }

    /**
     * <p>
     * Checks if the value can safely be converted to a int primitive.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return true if the value can be converted to an Integer.
     */
    public static boolean isInt(String value)
    {
        return (GenericTypeValidator.formatInt(value) != null);
    }

    /**
     * <p>
     * Checks if the value can safely be converted to a float primitive.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return true if the value can be converted to a Float.
     */
    public static boolean isFloat(String value)
    {
        return (GenericTypeValidator.formatFloat(value) != null);
    }

    /**
     * <p>
     * Checks if the value can safely be converted to a double primitive.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return true if the value can be converted to a Double.
     */
    public static boolean isDouble(String value)
    {
        return (GenericTypeValidator.formatDouble(value) != null);
    }

    /**
     * <p>
     * Checks if the field is a valid date. The <code>Locale</code> is used with <code>java.text.DateFormat</code>.
     * The setLenient method is set to <code>false</code> for all.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param locale
     *            The locale to use for the date format, defaults to the system default if null.
     * @return true if the value can be converted to a Date.
     */
    public static boolean isDate(String value, Locale locale)
    {
        if (value == null)
        {
            return false;
        }

        DateFormat formatter = null;
        if (locale != null)
        {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        }
        else
        {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        }

        formatter.setLenient(false);

        try
        {
            formatter.parse(value);
        } catch (ParseException e)
        {
            return false;
        }

        return true;
    }

    /**
     * <p>
     * Checks if the field is a valid date. The pattern is used with <code>java.text.SimpleDateFormat</code>. If
     * strict is true, then the length will be checked so '2/12/1999' will not pass validation with the format
     * 'MM/dd/yyyy' because the month isn't two digits. The setLenient method is set to <code>false</code> for all.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param datePattern
     *            The pattern passed to <code>SimpleDateFormat</code>.
     * @param strict
     *            Whether or not to have an exact match of the datePattern.
     * @return true if the value can be converted to a Date.
     */
    public static boolean isDate(String value, String datePattern, boolean strict)
    {
        if (value == null || datePattern == null || datePattern.length() <= 0)
        {

            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        formatter.setLenient(false);

        try
        {
            formatter.parse(value);
        } catch (ParseException e)
        {
            return false;
        }

        if (strict && (datePattern.length() != value.length()))
        {
            return false;
        }

        return true;
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(byte value, byte min, byte max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(int value, int min, int max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(float value, float min, float max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(short value, short min, short max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(long value, long min, long max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a value is within a range (min &amp; max specified in the vars attribute).
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @param min
     *            The minimum value of the range.
     * @param max
     *            The maximum value of the range.
     * @return true if the value is in the specified range.
     */
    public static boolean isInRange(double value, double min, double max)
    {
        return ((value >= min) && (value <= max));
    }

    /**
     * <p>
     * Checks if a field has a valid e-mail address.
     * </p>
     * 
     * @param value
     *            The value validation is being performed on.
     * @return true if the value is valid Email Address.
     */
    public static boolean isEmail(String value)
    {
        return validExpression(value, emailAddressPattern);
    }

    public static boolean validExpression(String value, String expression)
    {
        return validExpression(value, expression, false);
    }

    public static boolean validExpression(String value, String expression, boolean caseSensitive)
    {
        // if there is no value - don't do comparison
        // if a value is required, a required validator should be added to the field
        if (value == null || expression == null)
        {
            return false;
        }

        // string must not be empty
        String str = value.trim();
        if (str.length() == 0)
        {
            return false;
        }

        // match against expression
        Pattern pattern;
        if (caseSensitive)
        {
            pattern = Pattern.compile(expression);
        }
        else
        {
            pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        }

        String compare = str;

        Matcher matcher = pattern.matcher(compare);

        if (!matcher.matches())
        {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Checks if a field is a valid url address.
     * </p>
     * If you need to modify what is considered valid then consider using the UrlValidator directly.
     * 
     *            The value validation is being performed on.
     * @return true if the value is valid Url.
     */
    public static boolean isUrl(String url)
    {
        if (url == null)
        {
            return false;
        }

        if (url.startsWith("https://"))
        {
            // URL doesn't understand the https protocol, hack it
            url = "http://" + url.substring(8);
        }

        try
        {
            new URL(url);

            return true;
        } catch (MalformedURLException e)
        {
            return false;
        }
    }

    /**
     * <p>Checks if the value's length is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum length.
     * @return true if the value's length is less than the specified maximum.
     */
    public static boolean maxLength(String value, int max) {
        return (value.length() <= max);
    }

    /**
     * <p>Checks if the value's length is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum length.
     * @return true if the value's length is more than the specified minimum.
     */
    public static boolean minLength(String value, int min) {
        return (value.length() >= min);
    }
    
    /**
     * <p>Checks if the value's length is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum length.
     * @return true if the value's length is less than the specified maximum.
     */
    public static boolean maxByteLength(String value, int max) {
        if (null == value)
        {
            return false;
        }

        if (value.getBytes().length <= max)
        {
            return true;
        }
        return false;
    }

    /**
     * <p>Checks if the value's length is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum length.
     * @return true if the value's length is more than the specified minimum.
     */
    public static boolean minByteLength(String value, int min) {
        if (null == value)
        {
            return false;
        }

        if (value.getBytes().length >= min)
        {
            return true;
        }
        return false;
    }
    
    /**
     * <p>Checks if the value is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum numeric value.
     * @return true if the value is &gt;= the specified minimum.
     */
    public static boolean minValue(int value, int min) {
        return (value >= min);
    }

    /**
     * <p>Checks if the value is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum numeric value.
     * @return true if the value is &gt;= the specified minimum.
     */
    public static boolean minValue(long value, long min) {
        return (value >= min);
    }

    /**
     * <p>Checks if the value is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum numeric value.
     * @return true if the value is &gt;= the specified minimum.
     */
    public static boolean minValue(double value, double min) {
        return (value >= min);
    }

    /**
     * <p>Checks if the value is greater than or equal to the min.</p>
     *
     * @param value The value validation is being performed on.
     * @param min The minimum numeric value.
     * @return true if the value is &gt;= the specified minimum.
     */
    public static boolean minValue(float value, float min) {
        return (value >= min);
    }

    /**
     * <p>Checks if the value is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum numeric value.
     * @return true if the value is &lt;= the specified maximum.
     */
    public static boolean maxValue(int value, int max) {
        return (value <= max);
    }

    /**
     * <p>Checks if the value is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum numeric value.
     * @return true if the value is &lt;= the specified maximum.
     */
    public static boolean maxValue(long value, long max) {
        return (value <= max);
    }

    /**
     * <p>Checks if the value is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum numeric value.
     * @return true if the value is &lt;= the specified maximum.
     */
    public static boolean maxValue(double value, double max) {
        return (value <= max);
    }

    /**
     * <p>Checks if the value is less than or equal to the max.</p>
     *
     * @param value The value validation is being performed on.
     * @param max The maximum numeric value.
     * @return true if the value is &lt;= the specified maximum.
     */
    public static boolean maxValue(float value, float max) {
        return (value <= max);
    }    
    
    public static void main(String[] args)
    {
        String s = "123";
        long start = System.currentTimeMillis();
        // for (int i = 0; i < 1000000; i++)
        // {
       // System.out.println(Validate.isNumeric(s));
        // }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        s = "lsdnf-s@d1.d1";
       // System.out.println(Validate.isEmail(s));
        s="a11111111111";
        System.out.println(Validate.validExpression(s, "^[a-zA-Z]{1}[a-zA-Z0-9]{0,17}$"));

    }
}
