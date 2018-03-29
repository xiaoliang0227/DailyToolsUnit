package com.zyl.tools.dailytoolsunit.tool;

import android.text.TextUtils;

import com.zyl.tools.dailytoolsunit.interf.IMathTools;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitCommonConsts;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by zhaoyongliang on 2017/6/14.
 */

public class MathTools implements IMathTools {

    private static final String TAG = "MathTools";

    private MathTools() {

    }

    private static class SingletonHolder {
        private static final MathTools instance = new MathTools();
    }

    public static MathTools getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 转换为流量或者存储单位
     *
     * @param size
     * @return
     */
    @Override
    public String formatSizeUnit(long size) {
        String unit = "";
        float tmp = (float) (size * 1.0 / (1024));
        if (tmp < 1.0) {
            unit = size + "KB";
        } else if (tmp >= 1.0 && tmp < 1024.0f) {
            unit = round(tmp, 2, BigDecimal.ROUND_DOWN) + "MB";
        } else if (tmp >= 1024.0f && tmp < 1024 * 1024f) {
            unit = round(tmp / 1024, 2, BigDecimal.ROUND_DOWN) + "GB";
        } else if (tmp >= 1024 * 1024 && tmp < 1024 * 1024 * 1024) {
            unit = round(tmp / (1024 * 1024), 2, BigDecimal.ROUND_DOWN) + "TB";
        } else {
            unit = size + "KB";
        }
        return unit;
    }

    @Override
    public double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * 转换为整型
     *
     * @param object
     * @param defaultValue
     * @return
     */
    @Override
    public int convertToInt(Object object, int defaultValue) {
        if (null == object || TextUtils.isEmpty(object.toString()))
            return defaultValue;
        try {
            return Integer.valueOf(object.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(object.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * 转换为长整型
     *
     * @param object
     * @param defaultValue
     * @return
     */
    @Override
    public long convertToLong(Object object, long defaultValue) {
        if (null == object || TextUtils.isEmpty(object.toString()))
            return defaultValue;
        try {
            return Long.valueOf(object.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(object.toString()).longValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * convert byte to Mbit
     *
     * @param byteValue
     * @param pattern
     * @return
     */
    @Override
    public double byteToMbitWithPattern(long byteValue, String pattern) {
        double tmp = byteValue * 1.0 * 8 / 1024;
        DecimalFormat format = new DecimalFormat(pattern);
        return Double.parseDouble(format.format(tmp));
    }

    /**
     * convert Mbit to byte
     *
     * @param Mbit
     * @param pattern
     * @return
     */
    @Override
    public double MbitToByteWithPattern(double Mbit, String pattern) {
        double tmp = Mbit * 1.0 / 8 * 1024;
        DecimalFormat format = new DecimalFormat(pattern);
        return Double.parseDouble(format.format(tmp));
    }

    /**
     * 按格式格式化数值
     *
     * @param value
     * @param pattern
     * @return
     */
    @Override
    public String floatRound(double value, String pattern) {
        DecimalFormat fnum = new DecimalFormat(pattern);
        return fnum.format(value);
    }

    @Override
    public String floatRound2(double value) {
        return floatRound(value, "##0.00");
    }

    @Override
    public double formatDoubleWithPattern(double original, String pattern) {
        double tmp = original;
        try {
            DecimalFormat format = new DecimalFormat(pattern);
            tmp = Double.parseDouble(format.format(original));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    /**
     * 计算两个位置间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    @Override
    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * ToolsUnitCommonConsts.EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;

        ToolsUnitLogUtil.debug(TAG, "distance:" + s);
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
