package com.zyl.tools.dailytoolsunit.interf;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public interface IMathTools {

    /**
     * 转换为流量或者存储单位
     *
     * @param size
     * @return
     */
    String formatSizeUnit(long size);

    double round(double value, int scale, int roundingMode);

    /**
     * 转换为整型
     *
     * @param object
     * @param defaultValue
     * @return
     */
    int convertToInt(Object object, int defaultValue);

    /**
     * 转换为长整型
     *
     * @param object
     * @param defaultValue
     * @return
     */
    long convertToLong(Object object, long defaultValue);

    /**
     * convert byte to Mbit
     *
     * @param byteValue
     * @param pattern
     * @return
     */
    double byteToMbitWithPattern(long byteValue, String pattern);

    /**
     * convert Mbit to byte
     *
     * @param Mbit
     * @param pattern
     * @return
     */
    double MbitToByteWithPattern(double Mbit, String pattern);

    /**
     * 按格式格式化数值
     *
     * @param value
     * @param pattern
     * @return
     */
    String floatRound(double value, String pattern);

    String floatRound2(double value);

    double formatDoubleWithPattern(double original, String pattern);

    /**
     * 计算两个位置间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    double getDistance(double lat1, double lng1, double lat2, double lng2);

}
