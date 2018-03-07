package com.zyl.tools.dailytoolsunit.interf;

import com.zyl.tools.dailytoolsunit.enumeration.SecurityLevel;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhaoyongliang on 2017/6/13.
 */

public interface IStringTools {

    /**
     * 获取字符串长度
     *
     * @param str
     * @return
     */
    int length(String str);

    /**
     * 将字符转换为大写
     *
     * @param str
     * @return
     */
    String toUpperCase(String str);

    /**
     * 将字符串转换为小写
     *
     * @param str
     * @return
     */
    String toLowerCase(String str);

    /**
     * 将字符串按照regex分割，并转换为数组
     *
     * @param str
     * @param regex
     * @return
     */
    String[] split(String str, String regex);

    /**
     * 格式化字符串
     *
     * @param pattern
     * @param params
     * @return
     */
    String format(String pattern, Object... params);

    /**
     * 将regex替换为replacement
     *
     * @param content
     * @param regex
     * @param replacement
     * @return
     */
    String replaceAll(String content, String regex, String replacement);

    /**
     * 获取str在content中首次出现的位置
     *
     * @param content
     * @param str
     * @return
     */
    int indexOf(String content, String str);

    /**
     * 获取str在content中最后出现的位置
     *
     * @param content
     * @param str
     * @return
     */
    int lastIndexOf(String content, String str);

    /**
     * 判断str是否为空
     *
     * @param str
     * @return
     */
    boolean isEmpty(String str);

    /**
     * 获取指定位置之间的字符串
     *
     * @param content
     * @param beginIndex
     * @param endIndex
     * @return
     */
    String substring(String content, int beginIndex, int endIndex);

    /**
     * 获取指定位置之后的字符串
     *
     * @param content
     * @param beginIndex
     * @return
     */
    String substring(String content, int beginIndex);

    /**
     * 去除字符串中的空格
     *
     * @param str
     * @return
     */
    String trim(String str);

    /**
     * 检查是否为手机号码
     *
     * @param mobile
     * @return
     */
    boolean isMobileNO(String mobile);

    /**
     * 转换为字符串
     *
     * @param object
     * @param defaultValue
     * @return
     */
    String convertToString(Object object, String defaultValue);

    /**
     * 对链接进行编码
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    String urlEncode(String str) throws UnsupportedEncodingException;

    /**
     * 对链接进行编码
     *
     * @param str
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    String urlEncode(String str, String charset) throws UnsupportedEncodingException;

    String formatSizeUnitByKB(long size);

    String formatSizeUnitBigBForSpeed(long size);

    String formatSizeUnitByBytes(long size);

    String getTransferSpeed(long startTime, long transferLength);

    String[] objectArray2StringArray(Object[] objArr);

    boolean isLegalIpAddress(String addr);

    String getRandomNumber(int length);

    int getWordCount(String str);

    int getWifiSsidLength(String ssid);

    /**
     * 字符串转Mac
     *
     * @param str
     * @return
     */
    String stringToMac(String str);

    /**
     * 获取密码强度等级
     *
     * @param pPasswordStr
     * @return
     */
    SecurityLevel getPwdSecurityLevel(String pPasswordStr);

    /**
     * 删除双引号
     *
     * @param str
     * @return
     */
    String removeDoubleQuote(String str);

    /**
     * 格式化距离
     *
     * @param distance
     * @return
     */
    String distanceFormater(int distance);

    /**
     * 校验是否为合法的身份证号
     *
     * @param cardNo
     * @return
     */
    boolean checkIdCard(String cardNo);

    /**
     * 校验是否为合法的邮箱地址
     *
     * @param email
     * @return
     */
    boolean checkEmail(String email);
}
