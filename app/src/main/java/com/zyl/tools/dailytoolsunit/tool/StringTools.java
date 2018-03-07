package com.zyl.tools.dailytoolsunit.tool;

import android.text.TextUtils;

import com.zyl.tools.dailytoolsunit.enumeration.SecurityLevel;
import com.zyl.tools.dailytoolsunit.interf.IStringTools;
import com.zyl.tools.dailytoolsunit.util.IdCardUtil;
import com.zyl.tools.dailytoolsunit.util.ToolsUnitLogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoyongliang on 2017/6/13.
 */

public class StringTools implements IStringTools {

    private static final String TAG = "StringTools";

    public static StringTools getInstance() {
        return SingletonHolder.instance;
    }

    private StringTools() {

    }

    private static class SingletonHolder {
        private final static StringTools instance = new StringTools();
    }

    /**
     * 获取字符串长度
     *
     * @param str
     * @return
     */
    @Override
    public int length(String str) {
        return str.length();
    }

    /**
     * 将字符转换为大写
     *
     * @param str
     * @return
     */
    @Override
    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    /**
     * 将字符串转换为小写
     *
     * @param str
     * @return
     */
    @Override
    public String toLowerCase(String str) {
        return str.toLowerCase();
    }

    /**
     * 将字符串按照regex分割，并转换为数组
     *
     * @param str
     * @param regex
     * @return
     */
    @Override
    public String[] split(String str, String regex) {
        return str.split(regex);
    }

    /**
     * 格式化字符串
     *
     * @param pattern
     * @param params
     * @return
     */
    @Override
    public String format(String pattern, Object... params) {
        return String.format(pattern, params);
    }

    /**
     * 将regex替换为replacement
     *
     * @param regex
     * @param replacement
     * @return
     */
    @Override
    public String replaceAll(String content, String regex, String replacement) {
        return content.replaceAll(regex, replacement);
    }

    /**
     * 获取str在content中首次出现的位置
     *
     * @param content
     * @param str
     * @return
     */
    @Override
    public int indexOf(String content, String str) {
        return content.indexOf(str);
    }

    /**
     * 获取str在content中最后出现的位置
     *
     * @param content
     * @param str
     * @return
     */
    @Override
    public int lastIndexOf(String content, String str) {
        return content.lastIndexOf(str);
    }

    /**
     * 判断str是否为空
     *
     * @param str
     * @return
     */
    @Override
    public boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    /**
     * 获取指定位置之间的字符串
     *
     * @param beginIndex
     * @param endIndex
     * @return
     */
    @Override
    public String substring(String content, int beginIndex, int endIndex) {
        return content.substring(beginIndex, endIndex);
    }

    /**
     * 获取指定位置之后的字符串
     *
     * @param beginIndex
     * @return
     */
    @Override
    public String substring(String content, int beginIndex) {
        return content.substring(beginIndex);
    }

    /**
     * 去除字符串中的空格
     *
     * @param str
     * @return
     */
    @Override
    public String trim(String str) {
        return str.trim();
    }

    /**
     * 检查是否为手机号码
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean isMobileNO(String mobile) {
        Pattern p = Pattern.compile("^[1][34578][0-9]{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 转换为字符串
     *
     * @param object
     * @param defaultValue
     * @return
     */
    @Override
    public String convertToString(Object object, String defaultValue) {
        if (null == object || TextUtils.isEmpty(object.toString()))
            return defaultValue;
        return object.toString();
    }

    /**
     * 对链接进行编码
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String urlEncode(String str) throws UnsupportedEncodingException {
        return urlEncode(str, null);
    }

    /**
     * 对链接进行编码
     *
     * @param str
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String urlEncode(String str, String charset) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(charset)) {
            return URLEncoder.encode(str, "UTF-8");
        }
        return URLEncoder.encode(str, charset);
    }

    @Override
    public String formatSizeUnitByKB(long size) {
        if (size <= 0)
            return "0KB";
        final String[] units = new String[]{"KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public String formatSizeUnitBigBForSpeed(long size) {
        if (size <= 0)
            return "0B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public String formatSizeUnitByBytes(long size) {
        long tmp = size;
        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int count = 0;
        while (tmp > 1024) {
            tmp = tmp / 1024;
            count++;
        }
        return tmp + units[count];
    }

    @Override
    public String getTransferSpeed(long startTime, long transferLength) {
        String speed = null;
        long distance = System.currentTimeMillis() - startTime;
        // 如果小于1s
        if (distance < 1000) {
            distance = 1000;
            long intSpeed = (long) (transferLength * 1.0 / distance);
            speed = formatSizeUnitBigBForSpeed(intSpeed);
        } else {
            distance = distance / 1000;
            speed = formatSizeUnitByBytes(transferLength / distance);
        }
        return speed;
    }

    @Override
    public String[] objectArray2StringArray(Object[] objArr) {
        if (null == objArr) return null;
        String[] arr = new String[objArr.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = objArr[i].toString();
        }
        return arr;
    }

    @Override
    public boolean isLegalIpAddress(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }

    @Override
    public String getRandomNumber(int length) {
        // 使用SET以此保证写入的数据不重复
        Set<Integer> set = new HashSet<Integer>();
        // 随机数
        Random random = new Random();

        while (set.size() < length) {
            // nextInt返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）
            // 和指定值（不包括）之间均匀分布的 int 值。
            set.add(random.nextInt(10));
        }
        Iterator<Integer> iterator = set.iterator();
        String temp = "";
        while (iterator.hasNext()) {
            temp += iterator.next();
        }
        return temp;
    }

    @Override
    public int getWordCount(String str) {
        str = str.replaceAll("[^\\x00-\\xff]", "**");
        return str.length();
    }

    @Override
    public int getWifiSsidLength(String ssid) {
        int count = 0;
        try {
            count = ssid.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 字符串转Mac
     *
     * @param str
     * @return
     */
    @Override
    public String stringToMac(String str) {
        String mac = str;
        try {
            String tmp = null;
            if (mac.length() > 12) {
                tmp = str.substring(12);
            } else {
                tmp = mac;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= tmp.length(); i++) {
                if (i % 2 == 0) {
                    sb.append(tmp.substring(i - 2, i)).append(":");
                }
            }
            if (sb.length() != 0) {
                sb.setLength(sb.length() - 1);
            }
            mac = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    /**
     * 获取密码强度等级
     *
     * @param pPasswordStr
     * @return
     */
    @Override
    public SecurityLevel getPwdSecurityLevel(String pPasswordStr) {
        SecurityLevel level = SecurityLevel.NONE;
        if (TextUtils.isEmpty(pPasswordStr)) {
            return level;
        }
        int grade = 0;
        int index = 0;
        char[] pPsdChars = pPasswordStr.toCharArray();

        int numIndex = 0;
        int sLetterIndex = 0;
        int lLetterIndex = 0;
        int symbolIndex = 0;

        for (char pPsdChar : pPsdChars) {
            int ascll = pPsdChar;
            /*
             * 数字 48-57 A-Z 65 - 90 a-z 97 - 122 !"#$%&'()*+,-./ (ASCII码：33~47)
             * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
             * (ASCII码：123~126)
             */
            if (ascll >= 48 && ascll <= 57) {
                numIndex++;
            } else if (ascll >= 65 && ascll <= 90) {
                lLetterIndex++;
            } else if (ascll >= 97 && ascll <= 122) {
                sLetterIndex++;
            } else if ((ascll >= 33 && ascll <= 47)
                    || (ascll >= 58 && ascll <= 64)
                    || (ascll >= 91 && ascll <= 96)
                    || (ascll >= 123 && ascll <= 126)) {
                symbolIndex++;
            }
        }
        /*
         * 一、密码长度: 5 到 7 字符 5 分: 8-11 个字符15分，大于11个字符25分
         */
        if (pPsdChars.length <= 7 && pPsdChars.length >= 5) {
            index = 5;
        } else if (pPsdChars.length <= 11 && pPsdChars.length >= 8) {
            index = 15;
        } else if (pPsdChars.length > 11) {
            index = 25;
        }
        grade += index;

        /*
         * 二、字母: 0 分: 没有字母 10 分: 全都是小（大）写字母 20 分: 大小写混合字母
         */
        if (lLetterIndex == 0 && sLetterIndex == 0) {
            index = 0;
        } else if (lLetterIndex != 0 && sLetterIndex != 0) {
            index = 20;
        } else {
            index = 10;
        }
        grade += index;
        /*
         * 三、数字: 0 分: 没有数字 10 分: 1 个数字 20 分: 大于 1 个数字
         */
        if (numIndex == 0) {
            index = 0;
        } else if (numIndex == 1) {
            index = 10;
        } else {
            index = 20;
        }
        grade += index;

        /*
         * 四、符号: 0 分: 没有符号 10 分: 1 个符号 25 分: 大于 1 个符号
         */
        if (symbolIndex == 0) {
            index = 0;
        } else if (symbolIndex == 1) {
            index = 10;
        } else {
            index = 25;
        }
        grade += index;
        /*
         * 五、奖励: 2 分: 字母和数字 3 分: 字母、数字和符号 5 分: 大小写字母、数字和符号
         */
        if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0) {
            index = 2;
        } else {
            if (numIndex != 0 && symbolIndex != 0) {
                if (sLetterIndex != 0 && lLetterIndex != 0) {
                    index = 5;
                } else {
                    index = 3;
                }
            }
        }
        grade += index;

        /*
         * TODO(按需自定义评分标准)
         * 最后的评分标准: >= 80: 高 >= 60 && < 80: 中 >= 0 && < 60: 低
         */
        if (grade >= 60) {
            level = SecurityLevel.HIGH;
        } else if (grade >= 40 && grade < 60) {
            level = SecurityLevel.MIDDLE;
        } else if (grade >= 0 && grade < 40) {
            level = SecurityLevel.LOW;
        }
        return level;
    }

    /**
     * 删除双引号
     *
     * @param str
     * @return
     */
    @Override
    public String removeDoubleQuote(String str) {
        return str.replaceAll("\"", "");
    }

    /**
     * 格式化距离
     *
     * @param distance
     * @return
     */
    @Override
    public String distanceFormater(int distance) {
        String tmp = null;
        if (distance < 1000) {
            tmp = distance + "m";
        } else {
            DecimalFormat format = new DecimalFormat("#.##");
            tmp = format.format(distance * 1.0 / 1000) + "km";
        }
        ToolsUnitLogUtil.debug(TAG, String.format("distance:%d,tmp:%s", distance, tmp));
        return tmp;
    }

    /**
     * 校验是否为合法的身份证号
     *
     * @param cardNo
     * @return
     */
    @Override
    public boolean checkIdCard(String cardNo) {
        return IdCardUtil.isIdcard(cardNo);
    }

    /**
     * 校验是否为合法的邮箱地址
     *
     * @param email
     * @return
     */
    @Override
    public boolean checkEmail(String email) {
        return email.matches("[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)+");
    }

    public static void main(String[] args) {
        String idcard = "130706197909174393";
        String emailAddress = "443000038@qq.com";
        String mobile = "13816303587";
        System.out.println("is idcard:" + StringTools.getInstance().checkIdCard(idcard));
        System.out.println("is email address:" + StringTools.getInstance().checkEmail(emailAddress));
        System.out.println("is mobile:" + StringTools.getInstance().isMobileNO(mobile));
    }
}
