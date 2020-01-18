package io.grx.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class CharUtils {
    public static final String LAST_3_BYTE_UTF_CHAR = "\uFFFF";
    public static final String REPLACEMENT_CHAR = "\uFFFD";
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public static String toValid3ByteUTF8String(String s) {
        final int length = s.length();
        StringBuilder b = new StringBuilder(length);
        for (int offset = 0; offset < length; ) {
            final int codepoint = s.codePointAt(offset);

            // do something with the codepoint
            if (codepoint > LAST_3_BYTE_UTF_CHAR.codePointAt(0)) {
                b.append(REPLACEMENT_CHAR);
            } else {
                if (Character.isValidCodePoint(codepoint)) {
                    b.appendCodePoint(codepoint);
                } else {
                    b.append(REPLACEMENT_CHAR);
                }
            }
            offset += Character.charCount(codepoint);
        }
        return b.toString();
    }

    public static String getIntArrayAsString(final int[] arrays,
                                                final String delimiter) {
        final StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arrays.length; i++) {
            sb.append(arrays[i]);
            if (i < arrays.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Split the provided String into an array list.
     *
     * @param source         source String. Can be null.
     * @param separatorChar  spearator String.
     * @return               ArrayList. If source is null, then return
     *                       an empty array list.
     */
    public static List<String> splitToArrayList(final String source,
                                                final String separatorChar) {
        final List<String> list = new ArrayList<String>();

        if (StringUtils.isNotEmpty(source)) {
            final String[] st = StringUtils.split(source, separatorChar);
            for (int i = 0; i < st.length; i++) {
                list.add(StringUtils.trim(st[i]));
            }
        }
        return list;
    }


    public static String getUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);

            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出现错误");
        }
    }

    public static Set<String> getPinyin(String chinese) {
        Set<String> result = new TreeSet<>();
        if (StringUtils.isBlank(chinese)) {
            return result;
        }

        for (int i = 0; i < chinese.length(); i++) {
            char c = chinese.charAt(i);
            String[] pArr = PinyinHelper.convertToPinyinArray(c, PinyinFormat.WITHOUT_TONE);

            if (pArr == null || pArr.length == 0) {
                continue;
            }

            Set<String> wordPinyins = new TreeSet<>();
            for (String p : pArr) {
                wordPinyins.add(String.valueOf(p.charAt(0)));
            }
            result = addFollowingPinyins(result, wordPinyins);
        }
        return result;
    }

    private static Set<String> addFollowingPinyins(Set<String> prefixes, Set<String> followings) {
        if (CollectionUtils.isEmpty(prefixes)) {
            return followings;
        }
        if (CollectionUtils.isEmpty(followings)) {
            return prefixes;
        }
        Set<String> result = new TreeSet<>();
        for (String p : prefixes) {
            for (String f : followings) {
                result.add(p + f);
            }
        }
        return result;
    }

    public static boolean isAllLetters(final String s) {
        String text = StringUtils.trim(s);
        if (StringUtils.isBlank(text)) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmail(String s) {
        return EMAIL_PATTERN.matcher(s).matches();
    }

    /**
     * 手机证件号中间数字脱敏
     * @param
     * @return
     */
    public static String maskMiddleChars(String s, int left, int right, char maskChar) {
        int maskLength = StringUtils.length(s) - left - right;

        if (maskLength <= 0) {
            return s;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maskLength; i++) {
            sb.append(maskChar);
        }
        return StringUtils.left(s, left) + sb.toString() + StringUtils.right(s, right);
    }

    /**
     * 手机证件号中间数字脱敏
     * @param
     * @return
     */
    public static String maskMiddleChars(String s, int left, int right) {
        return maskMiddleChars(s, left, right, '*');
    }
}
