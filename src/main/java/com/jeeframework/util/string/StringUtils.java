
package com.jeeframework.util.string;

import com.jeeframework.util.classes.ObjectUtils;
import com.jeeframework.util.collection.CollectionUtils;
import com.jeeframework.util.validate.Validate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    static String regEx = "[\\u4e00-\\u9fa5]";

    public static final String EMPTY = "";


    public static void isHanzi2(String str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
        }
    }

    public static boolean isHanzi(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isGB2312(String str) {

        char[] chars = str.toCharArray();
        boolean isGB2312 = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isGB2312 = true;
                    break;
                }
            }
        }
        return isGB2312;
    }

    /**
     * 得到matchStr在curStr里的出现次数
     *
     * @param curStr
     * @param matchStr
     * @return
     */
    public static int getStrCountInString(String curStr, String matchStr) {
        int retCount = 0;
        if (Validate.isNull(curStr)) {
            return retCount;
        }
        String tempStr = curStr;
        int inxPos = curStr.indexOf(matchStr);
        while (inxPos != -1) {
            tempStr = tempStr.substring(inxPos + matchStr.length());
            inxPos = tempStr.indexOf(matchStr);
            retCount++;
        }
        // retCount = curStr.split(matchStr).length;
        // if (retCount > 0) {
        // retCount = retCount - 1;
        // }
        return retCount;
    }

    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------

    /**
     * Check that the given CharSequence is neither <code>null</code> nor of length 0. Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
     * <p/>
     * <p/>
     * <pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given String is neither <code>null</code> nor of length 0. Note: Will return <code>true</code> for a String that purely consists of whitespace.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasLength(CharSequence)
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    /**
     * Check whether the given CharSequence has actual text. More specifically, returns <code>true</code> if the string not <code>null</code>, its length is greater than 0, and it contains at least
     * one non-whitespace character.
     * <p/>
     * <p/>
     * <pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not <code>null</code>, its length is greater than 0, and it does not contain whitespace only
     * @see java.lang.Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String has actual text. More specifically, returns <code>true</code> if the string not <code>null</code>, its length is greater than 0, and it contains at least one
     * non-whitespace character.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not empty and contains at least 1 whitespace character
     * @see java.lang.Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String contains any whitespace characters.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and contains at least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim <i>all</i> whitespace from the given String: leading, trailing, and inbetween characters.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        int index = 0;
        while (buf.length() > index) {
            if (Character.isWhitespace(buf.charAt(index))) {
                buf.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return buf.toString();
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim all occurences of the supplied leading character from the given String.
     *
     * @param str              the String to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    /**
     * Trim all occurences of the supplied trailing character from the given String.
     *
     * @param str               the String to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Test if the given String starts with the specified prefix, ignoring upper/lower case.
     *
     * @param str    the String to check
     * @param prefix the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * Test if the given String ends with the specified suffix, ignoring upper/lower case.
     *
     * @param str    the String to check
     * @param suffix the suffix to look for
     * @see java.lang.String#endsWith
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        if (str.endsWith(suffix)) {
            return true;
        }
        if (str.length() < suffix.length()) {
            return false;
        }

        String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
        String lcSuffix = suffix.toLowerCase();
        return lcStr.equals(lcSuffix);
    }

    /**
     * Test whether the given string matches the given substring at the given index.
     *
     * @param str       the original string (or StringBuffer)
     * @param index     the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count the occurrences of the substring in string s.
     *
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0, pos = 0, idx = 0;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Replace all occurences of a substring within a string with another string.
     *
     * @param inString   String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (inString == null) {
            return null;
        }
        if (oldPattern == null || newPattern == null) {
            return inString;
        }

        StringBuffer sbuf = new StringBuffer();
        // output StringBuffer we'll build up
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));

        // remember to append any characters to the right of a match
        return sbuf.toString();
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original String
     * @param pattern  the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given String.
     *
     * @param inString      the original String
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                out.append(c);
            }
        }
        return out.toString();
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------

    /**
     * Quote the given String with single quotes.
     *
     * @param str the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or <code>null<code> if the input was <code>null</code>
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a String with single quotes if it is a String; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified" returns "qualified" if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator     the separator
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a <code>String</code>, changing the first letter to upper case as per {@link Character#toUpperCase(char)}. No other letters are changed.
     *
     * @param str the String to capitalize, may be <code>null</code>
     * @return the capitalized String, <code>null</code> if null
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize a <code>String</code>, changing the first letter to lower case as per {@link Character#toLowerCase(char)}. No other letters are changed.
     *
     * @param str the String to uncapitalize, may be <code>null</code>
     * @return the uncapitalized String, <code>null</code> if null
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str.length());
        if (capitalize) {
            buf.append(Character.toUpperCase(str.charAt(0)));
        } else {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }

    /**
     * 取得文件的前面的名
     */
    public static String getFileNamePreffix(String fileName) {

        int pos = fileName.lastIndexOf(".");
        if (pos != -1) {
            return fileName.substring(0, pos);
        }
        return fileName;
    }

    /**
     * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename extension, or <code>null</code> if none
     */
    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
    }

    /**
     * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the path with stripped filename extension, or <code>null</code> if none
     */
    public static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
    }

    /**
     * Apply the given relative path to the given path, assuming standard Java folder separation (i.e. "/" separators);
     *
     * @param path         the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
     * <p/>
     * The result is convenient for path comparison. For other uses, notice that Windows separators ("\") are replaced by simple slashes.
     *
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(":");
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            pathToUse = pathToUse.substring(prefixIndex + 1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        List pathElements = new LinkedList();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            if (CURRENT_PATH.equals(pathArray[i])) {
                // Points to current directory - drop it.
            } else if (TOP_PATH.equals(pathArray[i])) {
                // Registering top path found.
                tops++;
            } else {
                if (tops > 0) {
                    // Merging path element with corresponding to top path.
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, pathArray[i]);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.add(0, TOP_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * Compare two paths after normalization of them.
     *
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * Parse the given <code>localeString</code> into a {@link Locale}.
     * <p/>
     * This is the inverse operation of {@link Locale#toString Locale's toString}.
     *
     * @param localeString the locale string, following <code>Locale's</code> <code>toString()</code> format ("en", "en_UK", etc); also accepts spaces as separators, as an alternative to underscores
     * @return a corresponding <code>Locale</code> instance
     */
    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = (parts.length > 0 ? parts[0] : "");
        String country = (parts.length > 1 ? parts[1] : "");
        String variant = "";
        if (parts.length >= 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }
        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------

    /**
     * Append the given String to the given String array, returning a new array consisting of the input array contents plus the given String.
     *
     * @param array the array to append to (can be <code>null</code>)
     * @param str   the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate the given String arrays into one, with overlapping array elements included twice.
     * <p/>
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }
        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Merge the given String arrays into one, with overlapping array elements only included once.
     * <p/>
     * The order of elements in the original arrays is preserved (with the exception of overlapping elements, which are only included on their first occurence).
     *
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }
        List result = new ArrayList();
        result.addAll(Arrays.asList(array1));
        for (int i = 0; i < array2.length; i++) {
            String str = array2[i];
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return toStringArray(result);
    }

    /**
     * Turn given source String array into sorted array.
     *
     * @param array the source array
     * @return the sorted array (never <code>null</code>)
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        }
        Arrays.sort(array);
        return array;
    }

    /**
     * Copy the given Collection into a String array. The Collection must contain String elements only.
     *
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the passed-in Collection was <code>null</code>)
     */
    public static String[] toStringArray(Collection collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    /**
     * Copy the given Enumeration into a String array. The Enumeration must contain String elements only.
     *
     * @param enumeration the Enumeration to copy
     * @return the String array (<code>null</code> if the passed-in Enumeration was <code>null</code>)
     */
    public static String[] toStringArray(Enumeration enumeration) {
        if (enumeration == null) {
            return null;
        }
        List list = Collections.list(enumeration);
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * Trim the elements of the given String array, calling <code>String.trim()</code> on each of them.
     *
     * @param array the original String array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
     *
     * @param array the String array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }
        Set set = new TreeSet();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }
        return toStringArray(set);
    }

    /**
     * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
     *
     * @param toSplit   the string to split
     * @param delimiter to split the string up with
     * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter (neither element includes the delimiter); or <code>null</code> if the delimiter wasn't
     * found in the given input String
     */
    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }
        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[]{beforeDelimiter, afterDelimiter};
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is then generated, with the left of the delimiter providing the key, and the right
     * of the delimiter providing the value.
     * <p/>
     * Will trim both the key and value before adding them to the <code>Properties</code> instance.
     *
     * @param array     the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to process was null or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is then generated, with the left of the delimiter providing the key, and the right
     * of the delimiter providing the value.
     * <p/>
     * Will trim both the key and value before adding them to the <code>Properties</code> instance.
     *
     * @param array         the array to process
     * @param delimiter     to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element prior to attempting the split operation (typically the quotation mark symbol), or <code>null</code> if no removal should
     *                      occur
     * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to process was <code>null</code> or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }
        Properties result = new Properties();
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            if (charsToDelete != null) {
                element = deleteAny(array[i], charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits empty tokens.
     * <p/>
     * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those characters can be used to separate tokens. A delimiter is always a single character; for
     * multi-character delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str        the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p/>
     * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those characters can be used to separate tokens. A delimiter is always a single character; for
     * multi-character delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str               the String to tokenize
     * @param delimiters        the delimiter characters, assembled as String (each of those characters is individually considered as delimiter)
     * @param trimTokens        trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that are empty after trimming; StringTokenizer will not consider subsequent delimiters as token in the
     *                          first place).
     * @return an array of the tokens (<code>null</code> if the input String was <code>null</code>)
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p/>
     * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather than as bunch of potential delimiter characters - in contrast to
     * <code>tokenizeToStringArray</code>.
     *
     * @param str       the input String
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p/>
     * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather than as bunch of potential delimiter characters - in contrast to
     * <code>tokenizeToStringArray</code>.
     *
     * @param str           the input String
     * @param delimiter     the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }
        List result = new ArrayList();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos = 0;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Convert a CSV list into an array of Strings.
     *
     * @param str the input String
     * @return an array of Strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
     *
     * @param str the input String
     * @return a Set of String entries in the list
     */
    public static Set commaDelimitedListToSet(String str) {
        Set set = new TreeSet();
        String[] tokens = commaDelimitedListToStringArray(str);
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i]);
        }
        return set;
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param coll   the Collection to display
     * @param delim  the delimiter to use (probably a ",")
     * @param prefix the String to start each element with
     * @param suffix the String to end each element with
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param coll  the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Convenience method to return a Collection as a CSV String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param coll the Collection to display
     * @return the delimited String
     */
    public static String collectionToCommaDelimitedString(Collection coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV) String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a String array as a CSV String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param arr the array to display
     * @return the delimited String
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    /**
     * 过滤json中的特殊字符 分两种情况，需要保留回车换行符号的，输出不能在js端把回车换行符号去掉。如textarea之类 还有一种情况，不需要保留所有数据的回车换行符号，输出js端不需要做和任何处理，java端调用过滤函数进行处理就可以
     *
     * @param s
     * @return
     */
    public static String string2Json(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 20);
        // sb.append('\"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        // sb.append('\"');

        return sb.toString();
    }

    /**
     * 返回字符串的字节字符串，最后一个字节如果是汉字则截取
     *
     * @param originString
     * @param start
     * @param end
     * @return
     */
    public static String cSubString(String originString, int start, int end) {
        if (Validate.isEmpty(originString)) {
            return "";
        }
        if (end < start) {
            return "";
        }

        char[] oriChar = originString.toCharArray();

        String returnStr = "";
        int currentNum = 0; // 当前处理到的字符在字符串中的位置
        for (int i = 0; i < oriChar.length; i++) {
            if (start <= currentNum && currentNum < end) {
                returnStr += oriChar[i];
            }
            currentNum += String.valueOf(oriChar[i]).getBytes().length;
        }
        return returnStr;
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     * </p>
     * <p/>
     * <p>
     * No separator is added to the joined String. Null objects or empty strings within the array are represented by empty strings.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.join(null)            = null
     * StringUtils.join([])              = ""
     * StringUtils.join([null])          = ""
     * StringUtils.join(["a", "b", "c"]) = "abc"
     * StringUtils.join([null, "", "a"]) = "a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array) {
        return join(array, null);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented by empty strings.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty strings within the array are represented by empty strings.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from. It is an error to pass in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. A <code>null</code> separator is the same as an empty String (""). Null objects or empty strings within the array are represented by empty
     * strings.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array     the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing the provided list of elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. A <code>null</code> separator is the same as an empty String (""). Null objects or empty strings within the array are represented by empty
     * strings.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array      the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from. It is an error to pass in an end index past the end of the array
     * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided <code>Iterator</code> into a single String containing the provided elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty strings within the iteration are represented by empty strings.
     * </p>
     * <p/>
     * <p>
     * See the examples here: {@link #join(Object[], char)}.
     * </p>
     *
     * @param iterator  the <code>Iterator</code> of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, <code>null</code> if null iterator input
     * @since 2.0
     */
    public static String join(Iterator iterator, char separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }

        // two or more elements
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided <code>Iterator</code> into a single String containing the provided elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. A <code>null</code> separator is the same as an empty String ("").
     * </p>
     * <p/>
     * <p>
     * See the examples here: {@link #join(Object[], String)}.
     * </p>
     *
     * @param iterator  the <code>Iterator</code> of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null iterator input
     */
    public static String join(Iterator iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }

        // two or more elements
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided <code>Collection</code> into a single String containing the provided elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. Null objects or empty strings within the iteration are represented by empty strings.
     * </p>
     * <p/>
     * <p>
     * See the examples here: {@link #join(Object[], char)}.
     * </p>
     *
     * @param collection the <code>Collection</code> of values to join together, may be null
     * @param separator  the separator character to use
     * @return the joined String, <code>null</code> if null iterator input
     * @since 2.3
     */
    public static String join(Collection collection, char separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    /**
     * <p>
     * Joins the elements of the provided <code>Collection</code> into a single String containing the provided elements.
     * </p>
     * <p/>
     * <p>
     * No delimiter is added before or after the list. A <code>null</code> separator is the same as an empty String ("").
     * </p>
     * <p/>
     * <p>
     * See the examples here: {@link #join(Object[], String)}.
     * </p>
     *
     * @param collection the <code>Collection</code> of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null iterator input
     * @since 2.3
     */
    public static String join(Collection collection, String separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String str1 = "zhongguo";
        String str2 = "dfdf中国d*@(@&#(@^#";

        isHanzi2(str2);

    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        //        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 判断两个字符串的编辑距离相似度
     *
     * @param str1
     * @param str2
     * @return
     */
    public static float getSimilarityByLevenshtein(String str1, String str2) {
        int dis = org.apache.commons.lang3.StringUtils.getLevenshteinDistance(str1, str2);
        float disF = new BigDecimal(dis).divide(new BigDecimal(Math.max(str1.length(), str2.length())), 2, BigDecimal.ROUND_HALF_UP).floatValue();

        float correlation = 1 - disF;
        return correlation;
    }

    /**
     * 简单描述： 转半角的函数(DBC case) 任意字符串 半角字符串 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * <p/>
     *
     * @param input i
     * @return tag
     */
    public static String toDBC(String input) {
        if (Validate.isEmpty(input)) {
            return " ";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    public static boolean isAlphaNotCHN(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        char ch;
        for (int i = 0; i < sz; i++) {
            ch = str.charAt(i);

            if (!isAlphaChar(ch)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaNumericNotCHN(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        char ch;
        for (int i = 0; i < sz; i++) {
            ch = str.charAt(i);

            if (!isAlphaNumericChar(ch)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaChar(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isAlphaNumericChar(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
    }


    public static String getEncodeUrl(String urlString, String encode) {

        StringBuilder buf = new StringBuilder();

        // byte[] rawdata = URLCodec.encodeUrl(URI.allowed_fragment,
        // EncodingUtil.getBytes(urlString, encode));
        // return new
        // String(EncodingUtil.getAsciiString(rawdata).toCharArray());

        int questIndex = urlString.indexOf('?');
        if (questIndex == -1) {
            return urlString;
        }
        String url = urlString.substring(0, questIndex);
        String queryString = urlString.substring(questIndex + 1, urlString.length());

        Map<String, String[]> paramMap = getParamsMap(queryString, encode);

        buf.append(url);

        if (paramMap.size() > 0) {
            buf.append("?");
            for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
                buf.append(e.getKey()).append("=").append(StringUtils.join(e.getValue(), ",")).append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }

        return buf.toString();
    }

    private static Map<String, String[]> getParamsMap(String queryString, String enc) {
        Map<String, String[]> paramsMap = new HashMap<String, String[]>();
        if (queryString != null && queryString.length() > 0) {
            int ampersandIndex, lastAmpersandIndex = 0;
            String subStr, param, value;
            String[] paramPair, values, newValues;
            do {
                ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                if (ampersandIndex > 0) {
                    subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }
                // ticket=ST-MjEwMTQwNzI3MA==-1328862542-xd-BE27A54218FA11939D0AEB97CADD0440
                int i = subStr.indexOf("=");
                if (i >= 0) {
                    param = subStr.substring(0, i);
                    value = (subStr.length() - 1) == i ? "" : subStr.substring(i + 1);
                } else {
                    param = subStr;
                    value = "";
                }
                /*
                 * paramPair = subStr.split("="); param = paramPair[0]; value =
                 * paramPair.length == 1 ? "" : paramPair[1];
                 */
                if (!Validate.isEmpty(enc)) {
                    try {
                        value = URLEncoder.encode(value, enc);
                    } catch (UnsupportedEncodingException ignored) {
                    }
                }
                if (paramsMap.containsKey(param)) {
                    values = paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }
                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }
        return paramsMap;
    }

    /**
     * 根据页面body获取字符编码
     *
     * @param html
     * @param defaultCharset
     * @return
     */
    public static String getCharSetByBody(String html, String defaultCharset) {

        String localCharset = "";

        html = html.replaceAll("([\\r\\t\\n]+)", "");
        String regex = "<meta(.*?)>";
        // <meta name="keywords"
        // content="httpclient httpclient自动获取页面编码设置进行字符编码,使httpclient适用所有网页抓取不乱码"
        // />
        // <META http-equiv=Content-Type content="text/html; charset=gb2312">
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String content = matcher.group(1);
            String str = getCharSet(content);
            if (!Validate.isEmpty(str)) {
                if (str.equalsIgnoreCase("gb2312")) {
                    localCharset = str;
                } else if (str.equalsIgnoreCase("utf-8")) {
                    localCharset = str;
                } else if (str.equalsIgnoreCase("gbk")) {
                    localCharset = str;
                } else if (str.equalsIgnoreCase("iso-8859-1")) {
                    localCharset = str;
                } else if (str.equalsIgnoreCase("euc-jp")) {
                    localCharset = str;
                }
                break;
            }
        }

        return localCharset;
    }


    public static boolean isMobile(String mobiles) {
        Pattern p1 = Pattern.compile("^1[358]{1}[0-9]{9}$");
        Matcher m1 = p1.matcher(mobiles);

        Pattern p2 = Pattern.compile("^852[69]{1}[0-9]{7}$");
        Matcher m2 = p2.matcher(mobiles);

        Pattern p3 = Pattern.compile("^88609[0-9]{8}$");
        Matcher m3 = p3.matcher(mobiles);

        return (m1.matches() || m2.matches() || m3.matches());
    }

    /**
     * 正则获取字符编码
     *
     * @param content
     * @return
     */
    public static String getCharSet(String content) {
        // http-equiv="Content-Type" content="text/html;charset=gb2312"
        String regex = ".*charset=\"?([^;\"]*).*";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    public static String getCharset(String content) {
        final String CHARSET_STRING = "charset";
        int index;
        String ret = "UTF-8";

        if (null != content) {
            index = content.toLowerCase().indexOf(CHARSET_STRING);// 默认第一处

            if (index != -1) {
                content = content.substring(index + CHARSET_STRING.length()).trim();
                if (content.startsWith("=")) {
                    content = content.substring(1).trim();
                    index = content.indexOf(";");
                    if (index != -1)
                        content = content.substring(0, index);
                    // remove any double quotes from around charset string
                    if (content.startsWith("\""))
                        content = content.replaceFirst("\"", "");

                    // remove any single quote from around charset string
                    if (content.startsWith("'"))
                        content = content.replaceFirst("'", "");

                    if (content.indexOf("\"") != -1) {
                        content = content.substring(0, content.indexOf("\"")).trim();
                    }

                    if (content.indexOf("'") != -1) {
                        content = content.substring(0, content.indexOf("'")).trim();
                    }

                    if (content.indexOf("/") != -1) {
                        content = content.substring(0, content.indexOf("/")).trim();
                    }

                    if (content.indexOf(">") != -1) {
                        content = content.substring(0, content.indexOf(">")).trim();
                    }

                    ret = findCharset(content, ret);

                }
            }
        }

        return (ret);
    }

    /**
     * Lookup a character set name. <em>Vacuous for JVM's without <code>java.nio.charset</code>.</em> This uses reflection so the code will still run under prior JDK's but in that case the default is
     * always returned.
     *
     * @param name     The name to look up. One of the aliases for a character set.
     * @param _default The name to return if the lookup fails.
     */
    public static String findCharset(String name, String _default) {
        String ret;

        try {
            Class cls;
            Method method;
            Object object;

            cls = Class.forName("java.nio.charset.Charset");
            method = cls.getMethod("forName", new Class[]{String.class});
            object = method.invoke(null, new Object[]{name});
            method = cls.getMethod("name", new Class[]{});
            object = method.invoke(object, new Object[]{});
            ret = (String) object;
        } catch (ClassNotFoundException cnfe) {
            // for reflection exceptions, assume the name is correct
            ret = name;
        } catch (NoSuchMethodException nsme) {
            // for reflection exceptions, assume the name is correct
            ret = name;
        } catch (IllegalAccessException ia) {
            // for reflection exceptions, assume the name is correct
            ret = name;
        } catch (InvocationTargetException ita) {
            // java.nio.charset.IllegalCharsetNameException
            // and java.nio.charset.UnsupportedCharsetException
            // return the default
            ret = _default;
            System.out.println("unable to determine cannonical charset name for " + name + " - using " + _default);
        }

        return (ret);
    }
}
