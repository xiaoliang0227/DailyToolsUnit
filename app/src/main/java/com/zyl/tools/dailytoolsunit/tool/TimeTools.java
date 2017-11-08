package com.zyl.tools.dailytoolsunit.tool;

import com.zyl.tools.dailytoolsunit.interf.ITimeTools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoyongliang on 2017/6/13.
 */

public class TimeTools implements ITimeTools {

    private static TimeTools instance;

    public static TimeTools getInstance() {
        if (null == instance) {
            instance = new TimeTools();
        }
        return instance;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param pattern
     * @return
     */
    @Override
    public String format(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    @Override
    public String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }
}
