package io.grx;

import org.apache.tomcat.util.buf.StringUtils;

import io.grx.common.utils.CharUtils;

public class TestExcel {

    public static void main (String[] args) throws Exception {

//        String str = "你好音乐";
//        System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
//        System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
//        System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
//        System.out.println(PinyinHelper.getShortPinyin(str)); // nhsj
//
//        System.out.println(Arrays.asList(PinyinHelper.convertToPinyinArray('a', PinyinFormat.WITHOUT_TONE)));
//
//        System.out.println(str.length());
//        for (int i = 0; i < str.length(); i++) {
//            System.out.println(str.charAt(i));
//        }

        System.out.println(StringUtils.join(CharUtils.getPinyin("李四")));

        System.out.println(Character.isLetter('a'));
        System.out.println(Character.isLetter('B'));
        System.out.println(Character.isLetter('1'));
    }

    /**
     * 是否是中文字符<br>
     * 包含中文标点符号<br>
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D) {
            return true;
        } else if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
