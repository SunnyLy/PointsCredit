package com.michel.pointscredit.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class PCStringUtils {
    public static final String PHONE_FORMAT = "^((17[0-9])|(13[0-9])|(15[0-3,5-9])|(18[0-9])|(199)|(198)|(166)|(145)|(147))\\d{8}$";
    public static final String EMAIL_FORMAT = "^[0-9a-zA-Z][_.0-9a-zA-Z-]{0,43}@([0-9a-zA-Z][0-9a-zA-Z-]{0,30}[0-9a-zA-Z].){1,4}[a-zA-Z]{2,4}$";
    public static final String VERIFY_CODE_FORMAT = "^\\d{4}$";
    public static final String PASSWORD_LEGAL_CHARACTERS_OLD = "[a-zA-Z0-9]{6,16}";//密码6-16位


    /**
     * 判断是否为正确的邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 判断参数是否为数字
     *
     * @param strNum 待判断的数字参数
     */
    public static boolean isNum(final String strNum) {
        return strNum.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 判断密码格式
     *
     * @param pwd
     * @return
     */
    public static boolean isRightPWDForm(@NonNull String pwd) {
        return pwd.matches(PASSWORD_LEGAL_CHARACTERS_OLD);
    }

    /**
     * 判断参数是否为手机号.
     */
    public static boolean isPhoneNum(final String strPhoneNum) {
        return Pattern.matches(PHONE_FORMAT, strPhoneNum);
    }

    /**
     * 判断给定字符是Ascill字符还是其它字符(如汉，日，韩文字符)
     */
    public static boolean isLetter(final char c) {
        int k = 0xFF;
        if (c / k == 0) {
            return true;
        }
        return false;
    }

    /**
     * 计算字符的长度  Ascii字符算一个长度 非Ascii字符算两个长度
     */
    public static int getCharLength(final char c) {
        if (isLetter(c)) {
            return 1;
        }
        return 2;
    }

    /**
     * 获取字符串的长度,
     */
    public static int getStringLength(final String strSource) {
        int iSrcLen = 0;
        char[] arrChars = strSource.toCharArray();
        for (char arrChar : arrChars) {
            iSrcLen += getCharLength(arrChar);
        }
        return iSrcLen;
    }

    /**
     * 截取字符串，若参数strSuffix不为null，则加上该参数作为后缀
     *
     * @param strSource 原始字符串
     * @param iSubLen   截取的长度
     * @param strSuffix 后缀字符串，null表示不需要后缀
     * @return 截取后的字符串
     */
    public static String sub(final String strSource, final int iSubLen,
                             final String strSuffix) {
        if (TextUtils.isEmpty(strSource)) {
            return strSource;
        }
        String strFilter = strSource.trim(); // 过滤首尾空字符
        int iLength = getStringLength(strFilter); // 字符的长度
        if (iLength <= iSubLen) {
            return strFilter; // 字符长度小于待截取的长度
        }
        int iNum = iSubLen; // 可截取字符的数量
        int iSubIndex = 0; // 截取位置的游标
        char[] arrChars = strFilter.toCharArray();
        int iArrLength = arrChars.length;
        char c = arrChars[iSubIndex];
        StringBuffer sbContent = new StringBuffer();
        iNum -= getCharLength(c);
        while (iNum > -1 && iSubIndex < iArrLength) {
            ++iSubIndex;
            sbContent.append(c);
            if (iSubIndex < iArrLength) {
                c = arrChars[iSubIndex];
                iNum -= getCharLength(c);
            }
        }
        strFilter = sbContent.toString();
        if (!TextUtils.isEmpty(strSuffix)) {
            strFilter += strSuffix;
        }
        return strFilter;
    }

    /**
     * 截取字符串，长度超出的部分用省略号替代
     *
     * @param strSource 原始字符串
     * @param iSubLen   截取的长度
     * @return 截取后的字符串
     */
    public static String subWithDots(final String strSource, final int iSubLen) {
        return sub(strSource, iSubLen, "...");
    }

    public static String object2Str(Object obj) {
        String result = null;
        if (obj != null) {
            result = (String) obj;
        }

        return result;
    }
}
